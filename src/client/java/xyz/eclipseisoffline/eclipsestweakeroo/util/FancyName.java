package xyz.eclipseisoffline.eclipsestweakeroo.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesGenericConfig;

public class FancyName {

    private static final Map<GameType, MutableComponent> GAMEMODE_TEXT = Map.of(
            GameType.SURVIVAL, Component.literal("S").withStyle(ChatFormatting.RED),
            GameType.CREATIVE, Component.literal("C").withStyle(ChatFormatting.GREEN),
            GameType.ADVENTURE, Component.literal("A").withStyle(ChatFormatting.YELLOW),
            GameType.SPECTATOR, Component.literal("SP").withStyle(ChatFormatting.BLUE)
    );

    private static final Map<String, BiFunction<LivingEntity, PlayerInfo, Component>> PLACEHOLDERS = Map.ofEntries(
            Map.entry("name", (livingEntity, playerInfo) -> {
                if (playerInfo != null && playerInfo.getTabListDisplayName() != null) {
                    return playerInfo.getTabListDisplayName();
                } else if (livingEntity != null) {
                    return livingEntity.getDisplayName();
                } else if (playerInfo != null) {
                    return PlayerTeam.formatNameForTeam(playerInfo.getTeam(), Component.literal(playerInfo.getProfile().getName()));
                }
                return null;
            }),
            Map.entry("rawname", ((livingEntity, playerInfo) -> {
                if (livingEntity != null) {
                    if (livingEntity instanceof Player player) {
                        return Component.literal(player.getGameProfile().getName());
                    }
                    return livingEntity.getName();
                } else if (playerInfo != null) {
                    return Component.literal(playerInfo.getProfile().getName());
                }
                return null;
            })),
            Map.entry("gamemode", (livingEntity, playerInfo) -> GAMEMODE_TEXT.get(playerInfo.getGameMode())),
            Map.entry("ping", (livingEntity, playerInfo) -> getPingText(playerInfo.getLatency())),
            Map.entry("health", (livingEntity, playerInfo) -> Component.literal(EclipsesTweakerooUtil.roundToOneDecimal(livingEntity.getHealth())).withStyle(ChatFormatting.RED)),
            Map.entry("uuid", (livingEntity, playerInfo) -> {
                if (livingEntity != null) {
                    return Component.literal(livingEntity.getStringUUID());
                } else if (playerInfo != null) {
                    return Component.literal(playerInfo.getProfile().getId().toString());
                }
                return null;
            }),
            Map.entry("team", (livingEntity, playerInfo) -> {
                if (livingEntity != null) {
                    return Objects.requireNonNull(livingEntity.getTeam()).getDisplayName();
                } else if (playerInfo != null) {
                    return Objects.requireNonNull(playerInfo.getTeam()).getDisplayName();
                }
                return null;
            }),
            Map.entry("key", (livingEntity, playerInfo) -> playerInfo.hasVerifiableChat()
                    ? Component.literal("KEY").withStyle(ChatFormatting.GREEN)
                    : Component.literal("NO KEY").withStyle(ChatFormatting.RED)),
            Map.entry("attack", (livingEntity, playerInfo) -> EclipsesTweakerooUtil.getAttackDamageText(livingEntity, EclipsesGenericConfig.ATTACK_PLACEHOLDER_CRITICAL.getBooleanValue())),
            Map.entry("armor", (livingEntity, playerInfo) -> EclipsesTweakerooUtil.getArmorText(livingEntity)),
            Map.entry("distance", (livingEntity, playerInfo) -> {
                Player player = Minecraft.getInstance().player;
                if (player == null || player.equals(livingEntity)) {
                    return null;
                }
                Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
                return Component.literal(EclipsesTweakerooUtil.roundToOneDecimal((float) cameraPos.distanceTo(livingEntity.position())))
                        .withStyle(ChatFormatting.BLUE);
            }),
            Map.entry("statuseffect", (livingEntity, playerInfo) -> {
                List<ResourceKey<MobEffect>> mobEffects = new ArrayList<>(EclipsesTweakerooUtil.getStatusEffectsFromParticles(livingEntity));
                if (mobEffects.isEmpty()) {
                    return null;
                }

                mobEffects.sort(Comparator.comparingInt((mobEffect -> BuiltInRegistries.MOB_EFFECT.getOrThrow(mobEffect).isBeneficial() ? 0 : 1)));
                StringBuilder statusEffectString = new StringBuilder();
                for (ResourceKey<MobEffect> mobEffect : mobEffects) {
                    String statusEffectIconString = StatusEffectCharacterLoader.MAP.get(mobEffect);
                    if (statusEffectIconString != null) {
                        statusEffectString.append(statusEffectIconString);
                    }
                }

                return Component.literal(statusEffectString.toString());
            }),
            Map.entry("horsestats", (livingEntity, playerListEntry) -> {
                if (livingEntity instanceof AbstractHorse horse) {
                    double movementSpeed = horse.getAttributeValue(Attributes.MOVEMENT_SPEED);
                    double jumpStrength = horse.getAttributeValue(Attributes.JUMP_STRENGTH);

                    movementSpeed *= 42.16;
                    MutableComponent text = Component.literal(Math.round(movementSpeed * 100D) / 100D + "m/s").withStyle(ChatFormatting.GOLD);
                    text.append(Component.literal("-").withStyle(ChatFormatting.RESET));
                    text.append(Component.literal(String.valueOf(Math.round(jumpStrength * 100D) / 100D)).withStyle(ChatFormatting.GREEN));
                    return text;
                }
                return null;
            }),
            Map.entry("rawhorsestats", (livingEntity, playerListEntry) -> {
                if (livingEntity instanceof AbstractHorse horse) {
                    double movementSpeed = horse.getAttributeValue(Attributes.MOVEMENT_SPEED);
                    double jumpStrength = horse.getAttributeValue(Attributes.JUMP_STRENGTH);

                    MutableComponent text = Component.literal(String.valueOf(Math.round(movementSpeed * 100D) / 100D)).withStyle(ChatFormatting.GOLD);
                    text.append(Component.literal("-").withStyle(ChatFormatting.RESET));
                    text.append(Component.literal(String.valueOf(Math.round(jumpStrength * 100D) / 100D)).withStyle(ChatFormatting.GREEN));
                    return text;
                }
                return null;
            })
    );

    public static Component applyFancyName(LivingEntity entity, PlayerInfo player) {
        MutableComponent fancyName = Component.empty();
        List<Component> elements = new ArrayList<>();

        for (String element : EclipsesGenericConfig.FANCY_NAME_ELEMENTS.getStrings()) {
            if (element.isEmpty()) {
                continue;
            }
            Component elementValue = Component.literal(element);
            if (element.startsWith("{")) {
                String placeholder = element.replace("{", "").replace("}", "");
                try {
                    elementValue = PLACEHOLDERS.get(placeholder).apply(entity, player);
                    if (elementValue == null) {
                        continue;
                    }
                } catch (NullPointerException exception) {
                    continue;
                }
            }
            elements.add(elementValue);
        }

        for (int i = 0; i < elements.size(); i++) {
            fancyName.append(elements.get(i));
            if (i < elements.size() - 1) {
                fancyName.append(" - ");
            }
        }

        return fancyName;
    }

    private static Component getPingText(int ping) {
        return Component.literal(ping + "ms").withStyle(getPingColor(ping));
    }

    private static ChatFormatting getPingColor(int ping) {
        if (ping <= 0) {
            return ChatFormatting.DARK_GRAY;
        } else if (ping <= 150) {
            return ChatFormatting.GREEN;
        } else if (ping <= 300) {
            return ChatFormatting.YELLOW;
        } else if (ping <= 600) {
            return ChatFormatting.GOLD;
        } else if (ping <= 1000) {
            return ChatFormatting.RED;
        }

        return ChatFormatting.DARK_RED;
    }
}

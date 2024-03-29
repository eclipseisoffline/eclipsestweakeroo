package xyz.eclipseisoffline.eclipsestweakeroo.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent.Literal;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import xyz.eclipseisoffline.eclipsestweakeroo.EclipsesTweakeroo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalGenericConfig;

public class FancyName {

    private static final Map<GameMode, MutableText> GAMEMODE_TEXT = Map.of(
            GameMode.SURVIVAL, Text.literal("S").formatted(Formatting.RED),
            GameMode.CREATIVE, Text.literal("C").formatted(Formatting.GREEN),
            GameMode.ADVENTURE, Text.literal("A").formatted(Formatting.YELLOW),
            GameMode.SPECTATOR, Text.literal("SP").formatted(Formatting.BLUE)
    );

    private static final Map<String, BiFunction<LivingEntity, PlayerListEntry, Text>> PLACEHOLDERS = Map.ofEntries(
            Map.entry("name", (livingEntity, playerListEntry) -> {
                if (playerListEntry != null && playerListEntry.getDisplayName() != null) {
                    return playerListEntry.getDisplayName();
                } else if (livingEntity != null) {
                    return livingEntity.getDisplayName();
                } else if (playerListEntry != null) {
                    return Team.decorateName(playerListEntry.getScoreboardTeam(),
                            Text.of(playerListEntry.getProfile().getName()));
                }
                return null;
            }),
            Map.entry("rawname", ((livingEntity, playerListEntry) -> {
                if (livingEntity != null) {
                    if (livingEntity instanceof PlayerEntity player) {
                        return Text.of(player.getGameProfile().getName());
                    }
                    return livingEntity.getName();
                } else if (playerListEntry != null) {
                    return Text.of(playerListEntry.getProfile().getName());
                }
                return null;
            })),
            Map.entry("gamemode", (livingEntity, playerListEntry) -> GAMEMODE_TEXT.get(
                    playerListEntry.getGameMode())),
            Map.entry("ping",
                    (livingEntity, playerListEntry) -> getPingText(playerListEntry.getLatency())),
            Map.entry("health", (livingEntity, playerListEntry) -> Text.literal(
                            String.valueOf(Math.ceil(livingEntity.getHealth())))
                    .formatted(Formatting.RED)),
            Map.entry("uuid", (livingEntity, playerListEntry) -> {
                if (livingEntity != null) {
                    return Text.of(livingEntity.getUuidAsString());
                } else if (playerListEntry != null) {
                    return Text.of(playerListEntry.getProfile().getId().toString());
                }
                return null;
            }),
            Map.entry("team", (livingEntity, playerListEntry) -> {
                if (livingEntity != null) {
                    return Objects.requireNonNull(livingEntity.getScoreboardTeam())
                            .getDisplayName();
                } else if (playerListEntry != null) {
                    return Objects.requireNonNull(playerListEntry.getScoreboardTeam())
                            .getDisplayName();
                }
                return null;
            }),
            Map.entry("key", (livingEntity, playerListEntry) -> playerListEntry.hasPublicKey()
                    ? Text.literal("KEY").formatted(Formatting.GREEN)
                    : Text.literal("NO KEY").formatted(Formatting.RED)),
            Map.entry("attack", (livingEntity, playerListEntry) -> {
                try {
                    return EclipsesTweakerooUtil.getAttackDamageText(livingEntity,
                            AdditionalGenericConfig.ATTACK_PLACEHOLDER_CRITICAL.getBooleanValue());
                } catch (IllegalArgumentException exception) {
                    return null;
                }
            }),
            Map.entry("armor",
                    (livingEntity, playerListEntry) -> EclipsesTweakerooUtil.getArmorText(
                            livingEntity)),
            Map.entry("distance", (livingEntity, playerListEntry) -> {
                PlayerEntity player = MinecraftClient.getInstance().player;
                assert player != null;
                if (player.equals(livingEntity)) {
                    return null;
                }
                Vec3d cameraPos = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
                return Text.literal(
                                String.valueOf(Math.floor(cameraPos.distanceTo(livingEntity.getPos()))))
                        .formatted(Formatting.BLUE);
            }),
            Map.entry("statuseffect", (livingEntity, playerListEntry) -> {
                List<StatusEffect> statusEffects = livingEntity.getActiveStatusEffects().keySet()
                        .stream().sorted(
                                Comparator.comparingInt(
                                        (statusEffect -> statusEffect.isBeneficial() ? 0 : 1)))
                        .toList();
                if (statusEffects.isEmpty()) {
                    return null;
                }
                StringBuilder statusEffectString = new StringBuilder();
                for (StatusEffect statusEffect : statusEffects) {
                    String statusEffectIconString = EclipsesTweakeroo.STATUS_EFFECT_CHARACTER_MAP.get(
                            statusEffect);
                    if (statusEffectIconString != null) {
                        statusEffectString.append(statusEffectIconString);
                    }
                }

                return Text.of(statusEffectString.toString());
            }),
            Map.entry("horsestats", (livingEntity, playerListEntry) -> {
                if (livingEntity instanceof AbstractHorseEntity horse) {
                    double movementSpeed = horse.getAttributeValue(
                            EntityAttributes.GENERIC_MOVEMENT_SPEED);
                    double jumpStrength = horse.getAttributeValue(
                            EntityAttributes.HORSE_JUMP_STRENGTH);

                    movementSpeed *= 42.16;
                    MutableText text = Text.literal(Math.round(movementSpeed * 100D) / 100D + "m/s")
                            .formatted(Formatting.GOLD);
                    text.append(Text.literal("-").formatted(Formatting.RESET));
                    text.append(Text.literal(String.valueOf(Math.round(jumpStrength * 100D) / 100D))
                            .formatted(Formatting.GREEN));
                    return text;
                }
                return null;
            }),
            Map.entry("rawhorsestats", (livingEntity, playerListEntry) -> {
                if (livingEntity instanceof AbstractHorseEntity horse) {
                    double movementSpeed = horse.getAttributeValue(
                            EntityAttributes.GENERIC_MOVEMENT_SPEED);
                    double jumpStrength = horse.getAttributeValue(
                            EntityAttributes.HORSE_JUMP_STRENGTH);

                    MutableText text = Text.literal(
                                    String.valueOf(Math.round(movementSpeed * 100D) / 100D))
                            .formatted(Formatting.GOLD);
                    text.append(Text.literal("-").formatted(Formatting.RESET));
                    text.append(Text.literal(String.valueOf(Math.round(jumpStrength * 100D) / 100D))
                            .formatted(Formatting.GREEN));
                    return text;
                }
                return null;
            })
    );

    public static Text applyFancyName(LivingEntity entity, PlayerListEntry player) {
        MutableText fancyName = MutableText.of(Literal.EMPTY);
        List<Text> elements = new ArrayList<>();

        for (String element : AdditionalGenericConfig.FANCY_NAME_ELEMENTS.getStrings()) {
            if (element.isEmpty()) {
                continue;
            }
            Text elementValue = Text.of(element);
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

    private static Text getPingText(int ping) {
        return Text.literal(ping + "ms").formatted(getPingColor(ping));
    }

    private static Formatting getPingColor(int ping) {
        if (ping <= 0) {
            return Formatting.DARK_GRAY;
        } else if (ping <= 150) {
            return Formatting.GREEN;
        } else if (ping <= 300) {
            return Formatting.YELLOW;
        } else if (ping <= 600) {
            return Formatting.GOLD;
        } else if (ping <= 1000) {
            return Formatting.RED;
        }

        return Formatting.DARK_RED;
    }
}

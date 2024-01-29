package xyz.eclipseisoffline.eclipsestweakeroo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeModifierCreator;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent.Literal;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalGenericConfig;

public class FancyName {

    private static final Map<GameMode, MutableText> GAMEMODE_TEXT = Map.of(
            GameMode.SURVIVAL, MutableText.of(new Literal("S")).setStyle(Style.EMPTY.withColor(
                    Formatting.RED)),
            GameMode.CREATIVE, MutableText.of(new Literal("C")).setStyle(Style.EMPTY.withColor(
                    Formatting.GREEN)),
            GameMode.ADVENTURE, MutableText.of(new Literal("A")).setStyle(Style.EMPTY.withColor(
                    Formatting.YELLOW)),
            GameMode.SPECTATOR, MutableText.of(new Literal("SP")).setStyle(Style.EMPTY.withColor(
                    Formatting.BLUE))
    );

    private static final Map<String, BiFunction<LivingEntity, PlayerListEntry, Text>> PLACEHOLDERS = Map.ofEntries(
            Map.entry("name", (livingEntity, playerListEntry) -> {
                if (livingEntity != null) {
                    return livingEntity.getDisplayName();
                } else if (playerListEntry != null) {
                    return Team.decorateName(playerListEntry.getScoreboardTeam(),
                            Text.literal(playerListEntry.getProfile().getName()));
                }
                return null;
            }),
            Map.entry("gamemode", (livingEntity, playerListEntry) -> GAMEMODE_TEXT.get(playerListEntry.getGameMode())),
            Map.entry("ping", (livingEntity, playerListEntry) -> getPingText(playerListEntry.getLatency())),
            Map.entry("health", (livingEntity, playerListEntry) -> MutableText.of(
                            new Literal(String.valueOf(Math.ceil(livingEntity.getHealth()))))
                    .setStyle(Style.EMPTY.withColor(Formatting.RED))),
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
                    ? MutableText.of(new Literal("KEY"))
                    .setStyle(Style.EMPTY.withColor(Formatting.GREEN))
                    : MutableText.of(new Literal("NO KEY"))
                            .setStyle(Style.EMPTY.withColor(Formatting.RED))),
            Map.entry("attack", (livingEntity, playerListEntry) -> {
                try {
                    EntityAttributeInstance attributeInstance = new EntityAttributeInstance(
                            EntityAttributes.GENERIC_ATTACK_DAMAGE, (instance) -> {
                    });
                    attributeInstance.setBaseValue(livingEntity.getAttributeBaseValue(
                            EntityAttributes.GENERIC_ATTACK_DAMAGE));
                    livingEntity.getStackInHand(Hand.MAIN_HAND)
                            .getAttributeModifiers(EquipmentSlot.MAINHAND)
                            .get(EntityAttributes.GENERIC_ATTACK_DAMAGE)
                            .forEach((attributeInstance::addTemporaryModifier));
                    livingEntity.getActiveStatusEffects().forEach((statusEffect, instance) -> {
                        AttributeModifierCreator attackModifier = statusEffect.getAttributeModifiers().get(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                        if (attackModifier != null) {
                            attributeInstance.addTemporaryModifier(attackModifier.createAttributeModifier(instance.getAmplifier()));
                        }
                    });

                    float base = (float) attributeInstance.getValue();
                    float enchantments = EnchantmentHelper.getAttackDamage(livingEntity.getStackInHand(Hand.MAIN_HAND), EntityGroup.DEFAULT);
                    float criticalDamage = (float) (base * 1.5) + enchantments;
                    float attackDamage = base + enchantments;

                    MutableText attack = MutableText.of(new Literal(String.valueOf(attackDamage)))
                            .setStyle(Style.EMPTY.withColor(Formatting.YELLOW));
                    if (livingEntity instanceof PlayerEntity && AdditionalGenericConfig.ATTACK_PLACEHOLDER_CRITICAL.getBooleanValue()) {
                        attack.append(MutableText.of(new Literal("+" + (criticalDamage - attackDamage))).setStyle(Style.EMPTY.withColor(Formatting.RED)));
                    }
                    return attack;
                } catch (IllegalArgumentException exception) {
                    return null;
                }
            }),
            Map.entry("armor", (livingEntity, playerListEntry) -> {
                try {
                    return MutableText.of(new Literal(String.valueOf(
                                    livingEntity.getAttributeValue(EntityAttributes.GENERIC_ARMOR))))
                            .setStyle(Style.EMPTY.withColor(Formatting.YELLOW));
                } catch (IllegalArgumentException exception) {
                    return null;
                }
            }),
            Map.entry("xp", (livingEntity, playerListEntry) -> {
                if (livingEntity instanceof PlayerEntity player) {
                    return MutableText.of(new Literal(String.valueOf(player.experienceLevel))).setStyle(Style.EMPTY.withColor(Formatting.GREEN));
                }
                return null;
            }),
            Map.entry("distance", (livingEntity, playerListEntry) -> {
                PlayerEntity player = MinecraftClient.getInstance().player;
                assert player != null;
                if (player.equals(livingEntity)) {
                    return null;
                }
                return MutableText.of(new Literal(String.valueOf(Math.ceil(livingEntity.distanceTo(player))))).setStyle(Style.EMPTY.withColor(Formatting.RED));
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
        return MutableText.of(new Literal(ping + "ms")).setStyle(getPingStyle(ping));
    }

    private static Style getPingStyle(int ping) {
        if (ping <= 0) {
            return Style.EMPTY.withColor(Formatting.DARK_GRAY);
        } else if (ping <= 150) {
            return Style.EMPTY.withColor(Formatting.GREEN);
        } else if (ping <= 300) {
            return Style.EMPTY.withColor(Formatting.YELLOW);
        } else if (ping <= 600) {
            return Style.EMPTY.withColor(Formatting.GOLD);
        } else if (ping <= 1000) {
            return Style.EMPTY.withColor(Formatting.RED);
        }

        return Style.EMPTY.withColor(Formatting.DARK_RED);
    }
}

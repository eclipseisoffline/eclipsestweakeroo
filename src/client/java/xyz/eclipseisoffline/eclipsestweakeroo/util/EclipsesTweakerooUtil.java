package xyz.eclipseisoffline.eclipsestweakeroo.util;

import fi.dy.masa.malilib.gui.Message.MessageType;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.tweakeroo.config.Configs;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;

import java.util.Map;

public class EclipsesTweakerooUtil {

    private static final int MAX_DURATION_SECONDS_EFFECT_TEXT = 3600;
    private static final double DURABILITY_WARNING = 0.9;
    private static final Map<MobEffect, ChatFormatting> EFFECT_COLOURS = Map.ofEntries(
            Map.entry(MobEffects.MOVEMENT_SPEED, ChatFormatting.WHITE),
            Map.entry(MobEffects.MOVEMENT_SLOWDOWN, ChatFormatting.DARK_GRAY),
            Map.entry(MobEffects.DIG_SPEED, ChatFormatting.GOLD),
            Map.entry(MobEffects.DIG_SLOWDOWN, ChatFormatting.GRAY),
            Map.entry(MobEffects.DAMAGE_BOOST, ChatFormatting.DARK_RED),
            Map.entry(MobEffects.JUMP, ChatFormatting.DARK_AQUA),
            Map.entry(MobEffects.CONFUSION, ChatFormatting.DARK_GREEN),
            Map.entry(MobEffects.REGENERATION, ChatFormatting.RED),
            Map.entry(MobEffects.DAMAGE_RESISTANCE, ChatFormatting.GRAY),
            Map.entry(MobEffects.FIRE_RESISTANCE, ChatFormatting.GOLD),
            Map.entry(MobEffects.WATER_BREATHING, ChatFormatting.BLUE),
            Map.entry(MobEffects.INVISIBILITY, ChatFormatting.WHITE),
            Map.entry(MobEffects.BLINDNESS, ChatFormatting.DARK_GRAY),
            Map.entry(MobEffects.NIGHT_VISION, ChatFormatting.BLUE),
            Map.entry(MobEffects.HUNGER, ChatFormatting.YELLOW),
            Map.entry(MobEffects.WEAKNESS, ChatFormatting.GRAY),
            Map.entry(MobEffects.POISON, ChatFormatting.GREEN),
            Map.entry(MobEffects.WITHER, ChatFormatting.DARK_GRAY),
            Map.entry(MobEffects.HEALTH_BOOST, ChatFormatting.RED),
            Map.entry(MobEffects.ABSORPTION, ChatFormatting.AQUA),
            Map.entry(MobEffects.SATURATION, ChatFormatting.RED),
            Map.entry(MobEffects.GLOWING, ChatFormatting.WHITE),
            Map.entry(MobEffects.LEVITATION, ChatFormatting.WHITE),
            Map.entry(MobEffects.LUCK, ChatFormatting.GREEN),
            Map.entry(MobEffects.UNLUCK, ChatFormatting.DARK_RED),
            Map.entry(MobEffects.SLOW_FALLING, ChatFormatting.GRAY),
            Map.entry(MobEffects.CONDUIT_POWER, ChatFormatting.BLUE),
            Map.entry(MobEffects.DOLPHINS_GRACE, ChatFormatting.BLUE),
            Map.entry(MobEffects.BAD_OMEN, ChatFormatting.GRAY),
            Map.entry(MobEffects.HERO_OF_THE_VILLAGE, ChatFormatting.GREEN),
            Map.entry(MobEffects.DARKNESS, ChatFormatting.DARK_GRAY)
    );

    private EclipsesTweakerooUtil() {}

    public static boolean shouldWarnDurability(ItemStack itemStack) {
        return itemStack.isDamageableItem() && itemStack.getDamageValue() >= (DURABILITY_WARNING * itemStack.getMaxDamage());
    }

    public static void showLowDurabilityWarning(ItemStack itemStack, boolean actionBar) {
        if (actionBar) {
            InfoUtils.showGuiOrActionBarMessage(MessageType.WARNING,
                    itemStack.getHoverName().getString() + " is at low durability! "
                            + (itemStack.getMaxDamage() - itemStack.getDamageValue()) + "/" + itemStack.getMaxDamage());
            return;
        }
        InfoUtils.showGuiOrInGameMessage(MessageType.WARNING,
                itemStack.getHoverName().getString() + " is at low durability! "
                        + (itemStack.getMaxDamage() - itemStack.getDamageValue()) + "/" + itemStack.getMaxDamage());
    }

    public static Component getDurationTextWithStyle(MobEffectInstance effect) {
        assert Minecraft.getInstance().level != null;
        int durationSeconds = effect.getDuration() / 20 / 1000;

        MutableComponent durationText;
        if (durationSeconds >= MAX_DURATION_SECONDS_EFFECT_TEXT) {
            durationText = Component.literal("**:**");
        } else {
            durationText = (MutableComponent) MobEffectUtil.formatDuration(effect, 1);
        }

        durationText.withStyle(EFFECT_COLOURS.getOrDefault(effect.getEffect(), ChatFormatting.WHITE));
        return durationText;
    }

    public static Component getAttackDamageText(LivingEntity entity, boolean critical) {
        // TODO cache?
        if (entity.getAttribute(Attributes.ATTACK_DAMAGE) == null) {
            return null;
        }
        AttributeInstance temporaryInstance = new AttributeInstance(Attributes.ATTACK_DAMAGE, (instance) -> {});
        temporaryInstance.setBaseValue(entity.getAttributeBaseValue(Attributes.ATTACK_DAMAGE));

        ItemStack mainHand = entity.getItemInHand(InteractionHand.MAIN_HAND);
        mainHand.getAttributeModifiers(EquipmentSlot.MAINHAND)
                .get(Attributes.ATTACK_KNOCKBACK)
                .forEach(temporaryInstance::addTransientModifier);

        if (!entity.getActiveEffects().isEmpty()) {
            entity.getActiveEffectsMap().forEach((effect, instance) -> {
                AttributeModifier baseModifier = effect.getAttributeModifiers()
                        .get(Attributes.ATTACK_DAMAGE);
                if (baseModifier != null) {
                    AttributeModifier attackModifier = new AttributeModifier(baseModifier.getName(),
                            effect.getAttributeModifierValue(instance.getAmplifier(), baseModifier), baseModifier.getOperation());
                    temporaryInstance.addTransientModifier(attackModifier);
                }
            });
        }

        float base = (float) temporaryInstance.getValue();
        float enchantments = EnchantmentHelper.getDamageBonus(mainHand, MobType.UNDEFINED);
        float attackDamage = base + enchantments;
        float criticalDamage = entity instanceof Player ? (float) (base * 1.5 + enchantments) - attackDamage : 0;

        MutableComponent component = Component.literal(String.valueOf(attackDamage)).withStyle(ChatFormatting.YELLOW);
        if (criticalDamage > 0 && critical) {
            component.append(Component.literal("+" + criticalDamage).withStyle(ChatFormatting.RED));
        }
        return component;
    }

    public static Component getArmorText(LivingEntity entity) {
        int baseArmor = entity.getArmorValue();
        int armorToughness = Mth.ceil(entity
                .getAttributeValue(Attributes.ARMOR_TOUGHNESS));
        int enchantmentProtectionFactor = EnchantmentHelper.getDamageProtection(entity.getArmorSlots(), entity.damageSources().generic());

        if (baseArmor > 0) {
            MutableComponent armorText = Component.literal(String.valueOf(baseArmor)).withStyle(ChatFormatting.GRAY);
            if (armorToughness > 0) {
                armorText.append(Component.literal("+" + armorToughness).withStyle(ChatFormatting.WHITE));
            }
            if (enchantmentProtectionFactor > 0) {
                armorText.append(Component.literal("+" + enchantmentProtectionFactor).withStyle(ChatFormatting.LIGHT_PURPLE));
            }
            return armorText;
        }
        return null;
    }

    public static String roundToOneDecimal(float number) {
        return "%.1f".formatted(number);
    }

    public static boolean shouldDisableUse(Player player, InteractionHand hand) {
        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_OFFHAND_USE)
                && hand == InteractionHand.OFF_HAND) {
            return true;
        }

        if (EclipsesGenericConfig.TWEAK_DURABILITY_PREVENT_USE.getBooleanValue()
                && ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_DURABILITY_CHECK)
                && player.getItemInHand(hand).isDamageableItem()) {
            if (player.getItemInHand(hand).getDamageValue() < player.getItemInHand(hand).getMaxDamage() - EclipsesGenericConfig.DURABILITY_PREVENT_USE_THRESHOLD.getIntegerValue()) {
                return false;
            }
            showLowDurabilityWarning(player.getItemInHand(hand), true);
            return true;
        }
        return false;
    }

    public static boolean bossBarDisabled() {
        return TweakerooFinder.hasTweakeroo() && Configs.Disable.DISABLE_BOSS_BAR.getBooleanValue();
    }
}

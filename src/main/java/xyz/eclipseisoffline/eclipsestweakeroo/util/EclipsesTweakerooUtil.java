package xyz.eclipseisoffline.eclipsestweakeroo.util;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.malilib.gui.Message.MessageType;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.tweakeroo.config.FeatureToggle;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import xyz.eclipseisoffline.eclipsestweakeroo.mixin.EntityEffectParticleEffectAccessor;
import xyz.eclipseisoffline.eclipsestweakeroo.mixin.LivingEntityAccessor;

public class EclipsesTweakerooUtil {

    private static final double NANO_MILLI = 0.000001;
    private static final int MAX_DURATION_SECONDS_EFFECT_TEXT = 3600;
    private static final double DURABILITY_WARNING = 0.9;
    private static final Map<RegistryEntry<StatusEffect>, Formatting> EFFECT_COLOURS = Map.ofEntries(
            Map.entry(StatusEffects.SPEED, Formatting.WHITE),
            Map.entry(StatusEffects.SLOWNESS, Formatting.DARK_GRAY),
            Map.entry(StatusEffects.HASTE, Formatting.GOLD),
            Map.entry(StatusEffects.MINING_FATIGUE, Formatting.GRAY),
            Map.entry(StatusEffects.STRENGTH, Formatting.DARK_RED),
            Map.entry(StatusEffects.JUMP_BOOST, Formatting.DARK_AQUA),
            Map.entry(StatusEffects.NAUSEA, Formatting.DARK_GREEN),
            Map.entry(StatusEffects.REGENERATION, Formatting.RED),
            Map.entry(StatusEffects.RESISTANCE, Formatting.GRAY),
            Map.entry(StatusEffects.FIRE_RESISTANCE, Formatting.GOLD),
            Map.entry(StatusEffects.WATER_BREATHING, Formatting.BLUE),
            Map.entry(StatusEffects.INVISIBILITY, Formatting.WHITE),
            Map.entry(StatusEffects.BLINDNESS, Formatting.DARK_GRAY),
            Map.entry(StatusEffects.NIGHT_VISION, Formatting.BLUE),
            Map.entry(StatusEffects.HUNGER, Formatting.YELLOW),
            Map.entry(StatusEffects.WEAKNESS, Formatting.GRAY),
            Map.entry(StatusEffects.POISON, Formatting.GREEN),
            Map.entry(StatusEffects.WITHER, Formatting.DARK_GRAY),
            Map.entry(StatusEffects.HEALTH_BOOST, Formatting.RED),
            Map.entry(StatusEffects.ABSORPTION, Formatting.AQUA),
            Map.entry(StatusEffects.SATURATION, Formatting.RED),
            Map.entry(StatusEffects.GLOWING, Formatting.WHITE),
            Map.entry(StatusEffects.LEVITATION, Formatting.WHITE),
            Map.entry(StatusEffects.LUCK, Formatting.GREEN),
            Map.entry(StatusEffects.UNLUCK, Formatting.DARK_RED),
            Map.entry(StatusEffects.SLOW_FALLING, Formatting.GRAY),
            Map.entry(StatusEffects.CONDUIT_POWER, Formatting.BLUE),
            Map.entry(StatusEffects.DOLPHINS_GRACE, Formatting.BLUE),
            Map.entry(StatusEffects.BAD_OMEN, Formatting.GRAY),
            Map.entry(StatusEffects.HERO_OF_THE_VILLAGE, Formatting.GREEN),
            Map.entry(StatusEffects.DARKNESS, Formatting.DARK_GRAY)
    );
    private static final Int2ObjectMap<StatusEffect> STATUS_EFFECT_PARTICLE_COLORS = new Int2ObjectOpenHashMap<>();

    private EclipsesTweakerooUtil() {
    }

    public static List<IConfigBase> getDeclaredOptions(Class<?> clazz) {
        Field[] tweakerooFields = clazz.getDeclaredFields();
        List<IConfigBase> options = new ArrayList<>();
        for (Field field : tweakerooFields) {
            if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(
                    field.getModifiers())) {
                if (IConfigBase.class.isAssignableFrom(field.getType())) {
                    try {
                        options.add((IConfigBase) field.get(null));
                    } catch (IllegalAccessException e) {
                        // Should never happen
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        return options;
    }

    public static List<IHotkeyTogglable> getDeclaredHotkeyOptions(Class<?> clazz) {
        Field[] tweakerooFields = clazz.getDeclaredFields();
        List<IHotkeyTogglable> options = new ArrayList<>();
        for (Field field : tweakerooFields) {
            if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(
                    field.getModifiers())) {
                if (IHotkeyTogglable.class.isAssignableFrom(field.getType())) {
                    try {
                        options.add((IHotkeyTogglable) field.get(null));
                    } catch (IllegalAccessException e) {
                        // Should never happen
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        return options;
    }

    public static List<FeatureToggle> getDeclaredFeatureToggles(Class<?> clazz) {
        Field[] tweakerooFields = clazz.getDeclaredFields();
        List<FeatureToggle> options = new ArrayList<>();
        for (Field field : tweakerooFields) {
            if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(
                    field.getModifiers())) {
                if (FeatureToggle.class.isAssignableFrom(field.getType())) {
                    try {
                        options.add((FeatureToggle) field.get(null));
                    } catch (IllegalAccessException e) {
                        // Should never happen
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        return options;
    }

    public static boolean shouldWarnDurability(ItemStack itemStack) {
        return itemStack.isDamageable() && itemStack.getDamage() >= (DURABILITY_WARNING
                * itemStack.getMaxDamage());
    }

    public static void showLowDurabilityWarning(ItemStack itemStack, boolean actionBar) {
        if (actionBar) {
            InfoUtils.showGuiOrActionBarMessage(MessageType.WARNING,
                    itemStack.getName().getString() + " is at low durability! "
                            + (itemStack.getMaxDamage() - itemStack.getDamage()) + "/"
                            + itemStack.getMaxDamage());
            return;
        }
        InfoUtils.showGuiOrInGameMessage(MessageType.WARNING,
                itemStack.getName().getString() + " is at low durability! "
                        + (itemStack.getMaxDamage() - itemStack.getDamage()) + "/"
                        + itemStack.getMaxDamage());
    }

    public static Text getDurationTextWithStyle(StatusEffectInstance effect) {
        assert MinecraftClient.getInstance().world != null;
        int durationSeconds = (int) ((MinecraftClient.getInstance().world.getTickManager()
                .getMillisPerTick() * effect.getDuration()) / 1000);

        MutableText durationText;
        if (durationSeconds >= MAX_DURATION_SECONDS_EFFECT_TEXT) {
            durationText = Text.literal("**:**");
        } else {
            durationText = (MutableText) StatusEffectUtil.getDurationText(effect,
                    1, MinecraftClient.getInstance().world.getTickManager().getTickRate());
        }

        durationText.formatted(
                EFFECT_COLOURS.getOrDefault(effect.getEffectType(), Formatting.WHITE));
        return durationText;
    }

    public static Text getAttackDamageText(LivingEntity entity, boolean critical) {
        // TODO cache?
        EntityAttributeInstance temporaryInstance = new EntityAttributeInstance(
                EntityAttributes.GENERIC_ATTACK_DAMAGE, (instance) -> {});

        temporaryInstance.setBaseValue(entity.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));

        BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> modifierApplier =
                (attribute, modifier) -> {
            if (attribute == EntityAttributes.GENERIC_ATTACK_DAMAGE) {
                temporaryInstance.addTemporaryModifier(modifier);
            }
        };

        AttributeModifiersComponent attributeModifiers = entity
                .getStackInHand(Hand.MAIN_HAND)
                .get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        if (attributeModifiers != null) {
            attributeModifiers.applyModifiers(EquipmentSlot.MAINHAND, modifierApplier);
        }

        if (!entity.getActiveStatusEffects().isEmpty()) {
            entity.getActiveStatusEffects().forEach((effect, instance) -> {
                effect.value().forEachAttributeModifier(instance.getAmplifier(), modifierApplier);
            });
        } else {
            List<StatusEffect> activeParticleEffects = getStatusEffectsFromParticles(entity);
            for (StatusEffect activeEffect : activeParticleEffects) {
                activeEffect.forEachAttributeModifier(0, modifierApplier);
            }
        }

        float base = (float) temporaryInstance.getValue();
        float enchantments = EnchantmentHelper.getAttackDamage(entity.getStackInHand(Hand.MAIN_HAND), null);
        float attackDamage = base + enchantments;
        float criticalDamage = entity instanceof PlayerEntity
                ? (float) ((base * 1.5) + enchantments) - attackDamage : 0;

        MutableText attack = Text.literal(String.valueOf(attackDamage))
                .formatted(Formatting.YELLOW);
        if (criticalDamage > 0 && critical) {
            attack.append(Text.literal("+" + criticalDamage).formatted(Formatting.RED));
        }
        return attack;
    }

    public static Text getArmorText(LivingEntity entity) {
        int baseArmor = entity.getArmor();
        int armorToughness = MathHelper.ceil(entity
                .getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS));
        int enchantmentProtectionFactor = EnchantmentHelper
                .getProtectionAmount(entity.getArmorItems(),
                        entity.getWorld().getDamageSources().generic());

        if (baseArmor > 0) {
            MutableText armorText = Text.literal(String.valueOf(baseArmor))
                    .formatted(Formatting.GRAY);
            if (armorToughness > 0) {
                armorText.append(Text.literal("+" + armorToughness).formatted(Formatting.WHITE));
            }
            if (enchantmentProtectionFactor > 0) {
                armorText.append(Text.literal("+" + enchantmentProtectionFactor)
                        .formatted(Formatting.LIGHT_PURPLE));
            }
            return armorText;
        }
        return null;
    }

    public static List<StatusEffect> getStatusEffectsFromParticles(LivingEntity livingEntity) {
        List<ParticleEffect> potionParticles = livingEntity.getDataTracker().get(LivingEntityAccessor.getTrackedPotionSwirls());
        if (potionParticles.isEmpty()) {
            return List.of();
        }

        List<StatusEffect> statusEffects = new ArrayList<>();
        for (ParticleEffect particleEffect : potionParticles) {
            StatusEffect statusEffect = getStatusEffectFromParticle(particleEffect);
            if (statusEffect != null) {
                statusEffects.add(statusEffect);
            }
        }
        return List.copyOf(statusEffects);
    }

    public static StatusEffect getStatusEffectFromParticle(ParticleEffect particle) {
        ParticleType<?> type = particle.getType();

        if (type == ParticleTypes.TRIAL_OMEN) {
            return StatusEffects.TRIAL_OMEN.value();
        } else if (type == ParticleTypes.INFESTED) {
            return StatusEffects.INFESTED.value();
        } else if (type == ParticleTypes.ITEM_SLIME) {
            return StatusEffects.OOZING.value();
        } else if (type == ParticleTypes.RAID_OMEN) {
            return StatusEffects.RAID_OMEN.value();
        } else if (type == ParticleTypes.ITEM_COBWEB) {
            return StatusEffects.WEAVING.value();
        } else if (type == ParticleTypes.SMALL_GUST) {
            return StatusEffects.WIND_CHARGED.value();
        } else if (type == ParticleTypes.ENTITY_EFFECT) {
            int argb = ((EntityEffectParticleEffectAccessor) particle).getColor();
            int rgb = argb & 0x00FFFFFF;
            return STATUS_EFFECT_PARTICLE_COLORS.get(rgb);
        }

        return null;
    }

    public static void populateStatusEffectColorMap() {
        Registries.STATUS_EFFECT.forEach(statusEffect -> STATUS_EFFECT_PARTICLE_COLORS.put(statusEffect.getColor(), statusEffect));
    }

    public static int milliTime() {
        return (int) (System.nanoTime() * NANO_MILLI);
    }
}

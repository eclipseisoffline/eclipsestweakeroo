package xyz.eclipseisoffline.eclipsestweakeroo.util;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.malilib.gui.Message.MessageType;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.tweakeroo.config.FeatureToggle;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import org.apache.commons.lang3.mutable.MutableFloat;
import xyz.eclipseisoffline.eclipsestweakeroo.mixin.particle.ColorParticleOptionAccessor;
import xyz.eclipseisoffline.eclipsestweakeroo.mixin.entity.LivingEntityAccessor;

public class EclipsesTweakerooUtil {

    private static final double NANO_MILLI = 0.000001;
    private static final int MAX_DURATION_SECONDS_EFFECT_TEXT = 3600;
    private static final double DURABILITY_WARNING = 0.9;
    private static final Map<ResourceKey<MobEffect>, ChatFormatting> EFFECT_COLOURS = Map.ofEntries(
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
            Map.entry(MobEffects.DARKNESS, ChatFormatting.DARK_GRAY),
            Map.entry(MobEffects.INFESTED, ChatFormatting.GRAY),
            Map.entry(MobEffects.OOZING, ChatFormatting.GREEN),
            Map.entry(MobEffects.RAID_OMEN, ChatFormatting.DARK_GRAY),
            Map.entry(MobEffects.TRIAL_OMEN, ChatFormatting.AQUA),
            Map.entry(MobEffects.WEAVING, ChatFormatting.WHITE),
            Map.entry(MobEffects.WIND_CHARGED, ChatFormatting.BLUE)
    ).entrySet().stream().collect(Collectors.toUnmodifiableMap(e -> e.getKey().unwrapKey().orElseThrow(), Map.Entry::getValue));
    private static final Int2ReferenceMap<ResourceKey<MobEffect>> STATUS_EFFECT_PARTICLE_COLORS = new Int2ReferenceOpenHashMap<>();

    private EclipsesTweakerooUtil() {}

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
        TickRateManager tickRate = Minecraft.getInstance().level.tickRateManager();
        int durationSeconds = (int) (tickRate.millisecondsPerTick() * effect.getDuration()) / 1000;

        MutableComponent durationText;
        if (durationSeconds >= MAX_DURATION_SECONDS_EFFECT_TEXT) {
            durationText = Component.literal("**:**");
        } else {
            durationText = (MutableComponent) MobEffectUtil.formatDuration(effect, 1, tickRate.tickrate());
        }

        durationText.withStyle(EFFECT_COLOURS.getOrDefault(effect.getEffect().unwrapKey().orElseThrow(), ChatFormatting.WHITE));
        return durationText;
    }

    public static Component getAttackDamageText(LivingEntity entity, boolean critical) {
        // TODO cache?
        if (entity.getAttribute(Attributes.ATTACK_DAMAGE) == null) {
            return null;
        }
        AttributeInstance temporaryInstance = new AttributeInstance(Attributes.ATTACK_DAMAGE, (instance) -> {});

        temporaryInstance.setBaseValue(entity.getAttributeBaseValue(Attributes.ATTACK_DAMAGE));

        BiConsumer<Holder<Attribute>, AttributeModifier> modifierApplier = (attribute, modifier) -> {
            if (attribute == Attributes.ATTACK_DAMAGE) {
                temporaryInstance.addTransientModifier(modifier);
            }
        };

        ItemAttributeModifiers attributeModifiers = entity.getItemInHand(InteractionHand.MAIN_HAND).get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (attributeModifiers != null) {
            attributeModifiers.forEach(EquipmentSlot.MAINHAND, modifierApplier);
        }

        if (!entity.getActiveEffects().isEmpty()) {
            entity.getActiveEffectsMap().forEach((effect, instance) -> effect.value().createModifiers(instance.getAmplifier(), modifierApplier));
        } else {
            List<ResourceKey<MobEffect>> activeParticleEffects = getStatusEffectsFromParticles(entity);
            for (ResourceKey<MobEffect> activeEffect : activeParticleEffects) {
                BuiltInRegistries.MOB_EFFECT.getOrThrow(activeEffect).value().createModifiers(0, modifierApplier);
            }
        }

        MutableFloat attackDamage = new MutableFloat(temporaryInstance.getValue());
        float criticalBase = attackDamage.floatValue();

        forEachEnchantment(entity, (enchantment, level) -> {
            List<ConditionalEffect<EnchantmentValueEffect>> damageEffects = enchantment.value().getEffects(EnchantmentEffectComponents.DAMAGE);
            for (ConditionalEffect<EnchantmentValueEffect> damageEffect : damageEffects) {
                if (damageEffect.requirements().isEmpty()) {
                    attackDamage.setValue(damageEffect.effect().process(level, entity.getRandom(), attackDamage.getValue()));
                }
            }
        });

        float criticalDamage = entity instanceof Player ? (float) ((criticalBase * 1.5) + (attackDamage.floatValue() - criticalBase)) - attackDamage.floatValue() : 0;

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

        MutableFloat mutableProtectionFactor = new MutableFloat();
        forEachEnchantment(entity, (enchantment, level) -> {
            List<ConditionalEffect<EnchantmentValueEffect>> damageProtectionEffects = enchantment.value().getEffects(EnchantmentEffectComponents.DAMAGE_PROTECTION);
            for (ConditionalEffect<EnchantmentValueEffect> effect : damageProtectionEffects) {
                // Empty requirements = effect activates always, OR when the only requirement is that the damage source doesn't bypass invulnerability
                // In vanilla Minecraft, only the Protection enchantment matches these requirements
                boolean emptyRequirements = effect.requirements().isEmpty();

                if (!emptyRequirements && effect.requirements().get() instanceof DamageSourceCondition damageSourceProperties) {
                    if (damageSourceProperties.predicate().isEmpty()) {
                        emptyRequirements = true;
                    } else {
                        DamageSourcePredicate sourcePredicate = damageSourceProperties.predicate().get();
                        if (sourcePredicate.directEntity().isEmpty() && sourcePredicate.sourceEntity().isEmpty() && sourcePredicate.isDirect().isEmpty()) {
                            List<TagPredicate<DamageType>> tags = sourcePredicate.tags();
                            if (tags.isEmpty()) {
                                emptyRequirements = true;
                            } else {
                                boolean onlyInvulnerable = true;
                                for (TagPredicate<DamageType> tag : tags) {
                                    if (tag.tag() != DamageTypeTags.BYPASSES_INVULNERABILITY) {
                                        onlyInvulnerable = false;
                                        break;
                                    }
                                }
                                if (onlyInvulnerable) {
                                    emptyRequirements = true;
                                }
                            }
                        }
                    }
                }

                if (emptyRequirements) {
                    mutableProtectionFactor.setValue(effect.effect().process(level, entity.getRandom(), mutableProtectionFactor.getValue()));
                }
            }
        });
        int enchantmentProtectionFactor = mutableProtectionFactor.intValue();

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

    public static List<ResourceKey<MobEffect>> getStatusEffectsFromParticles(LivingEntity entity) {
        List<ParticleOptions> potionParticles = entity.getEntityData().get(LivingEntityAccessor.getDataEffectParticles());
        if (potionParticles.isEmpty()) {
            return List.of();
        }

        List<ResourceKey<MobEffect>> statusEffects = new ArrayList<>();
        for (ParticleOptions particleEffect : potionParticles) {
            ResourceKey<MobEffect> statusEffect = getStatusEffectFromParticle(particleEffect);
            if (statusEffect != null) {
                statusEffects.add(statusEffect);
            }
        }
        return List.copyOf(statusEffects);
    }

    public static ResourceKey<MobEffect> getStatusEffectFromParticle(ParticleOptions particle) {
        ParticleType<?> type = particle.getType();

        if (type == ParticleTypes.TRIAL_OMEN) {
            return MobEffects.TRIAL_OMEN.unwrapKey().orElseThrow();
        } else if (type == ParticleTypes.INFESTED) {
            return MobEffects.INFESTED.unwrapKey().orElseThrow();
        } else if (type == ParticleTypes.ITEM_SLIME) {
            return MobEffects.OOZING.unwrapKey().orElseThrow();
        } else if (type == ParticleTypes.RAID_OMEN) {
            return MobEffects.RAID_OMEN.unwrapKey().orElseThrow();
        } else if (type == ParticleTypes.ITEM_COBWEB) {
            return MobEffects.WEAVING.unwrapKey().orElseThrow();
        } else if (type == ParticleTypes.SMALL_GUST) {
            return MobEffects.WIND_CHARGED.unwrapKey().orElseThrow();
        } else if (type == ParticleTypes.ENTITY_EFFECT) {
            int argb = ((ColorParticleOptionAccessor) particle).getColor();
            int rgb = argb & 0x00FFFFFF;
            return STATUS_EFFECT_PARTICLE_COLORS.get(rgb);
        }

        return null;
    }

    public static void populateStatusEffectColorMap() {
        BuiltInRegistries.MOB_EFFECT.registryKeySet()
                .forEach(statusEffect -> STATUS_EFFECT_PARTICLE_COLORS.put(BuiltInRegistries.MOB_EFFECT.getOrThrow(statusEffect).value().getColor(), statusEffect));
    }

    public static int milliTime() {
        return (int) (System.nanoTime() * NANO_MILLI);
    }

    public static String roundToOneDecimal(float number) {
        return "%.1f".formatted(number);
    }

    private static void forEachEnchantment(ItemStack stack, EquipmentSlot slot, BiConsumer<Holder<Enchantment>, Integer> enchantmentConsumer) {
        if (stack.isEmpty()) {
            return;
        }
        ItemEnchantments enchantments = stack.get(DataComponents.ENCHANTMENTS);
        if (enchantments == null || enchantments.isEmpty()) {
            return;
        }
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
            Holder<Enchantment> enchantment = entry.getKey();
            if (!enchantment.value().matchingSlot(slot)) {
                continue;
            }
            enchantmentConsumer.accept(enchantment, entry.getIntValue());
        }
    }

    private static void forEachEnchantment(LivingEntity entity, BiConsumer<Holder<Enchantment>, Integer> enchantmentConsumer) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            forEachEnchantment(entity.getItemBySlot(slot), slot, enchantmentConsumer);
        }
    }
}

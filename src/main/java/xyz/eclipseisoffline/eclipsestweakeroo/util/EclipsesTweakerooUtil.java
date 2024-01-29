package xyz.eclipseisoffline.eclipsestweakeroo.util;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.malilib.gui.Message.MessageType;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.tweakeroo.config.FeatureToggle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class EclipsesTweakerooUtil {

    private static final Map<StatusEffect, Formatting> EFFECT_COLOURS = Map.ofEntries(
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
    private static ServerAddress lastConnection = null;
    private static ServerInfo lastConnectionInfo = null;

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
        MutableText durationText = (MutableText) StatusEffectUtil.getDurationText(effect,
                1, MinecraftClient.getInstance().world.getTickManager().getTickRate());
        durationText.formatted(
                EFFECT_COLOURS.getOrDefault(effect.getEffectType(), Formatting.WHITE));
        return durationText;
    }

    public static ServerAddress getLastConnection() {
        return lastConnection;
    }

    public static void setLastConnection(ServerAddress lastConnection) {
        EclipsesTweakerooUtil.lastConnection = lastConnection;
    }

    public static ServerInfo getLastConnectionInfo() {
        return lastConnectionInfo;
    }

    public static void setLastConnectionInfo(ServerInfo lastConnectionInfo) {
        EclipsesTweakerooUtil.lastConnectionInfo = lastConnectionInfo;
    }
}

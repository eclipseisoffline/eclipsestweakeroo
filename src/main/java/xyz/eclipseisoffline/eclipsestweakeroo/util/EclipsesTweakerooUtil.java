package xyz.eclipseisoffline.eclipsestweakeroo.util;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.tweakeroo.config.FeatureToggle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent.Literal;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;

public class EclipsesTweakerooUtil {

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

    private EclipsesTweakerooUtil() {
    }

    public static void applyFancyName(PlayerListEntry playerListEntry,
            MutableText nameStart) {
        applyFancyName(playerListEntry, -1, nameStart);
    }

    public static void applyFancyName(int health, MutableText nameStart) {
        applyFancyName(null, health, nameStart);
    }

    public static void applyFancyName(PlayerListEntry playerListEntry, int health,
            MutableText nameStart) {
        if (playerListEntry != null) {
            nameStart.append(Text.of(" - "));
            nameStart.append(GAMEMODE_TEXT.get(playerListEntry.getGameMode()));
            nameStart.append(Text.of(" - "));
            nameStart.append(MutableText.of(new Literal(playerListEntry.getLatency() + "ms"))
                    .setStyle(getPingStyle(playerListEntry.getLatency())));
        }
        if (health > 0) {
            nameStart.append(Text.of(" - "));
            nameStart.append(MutableText.of(new Literal(String.valueOf(health)))
                    .setStyle(Style.EMPTY.withColor(Formatting.RED)));
        }
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

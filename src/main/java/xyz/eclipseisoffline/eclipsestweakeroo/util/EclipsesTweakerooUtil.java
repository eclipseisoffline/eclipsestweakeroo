package xyz.eclipseisoffline.eclipsestweakeroo.util;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.tweakeroo.config.FeatureToggle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class EclipsesTweakerooUtil {

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
}

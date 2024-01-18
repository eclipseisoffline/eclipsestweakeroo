package xyz.eclipseisoffline.eclipsestweakeroo.config;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class TweakerooConfigMixinHelper {

    private TweakerooConfigMixinHelper() {}

    public static List<IConfigBase> getDeclaredOptions(Class<?> clazz) {
        Field[] tweakerooFields = clazz.getDeclaredFields();
        List<IConfigBase> options = new ArrayList<>();
        for (Field field : tweakerooFields) {
            if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())) {
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
            if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())) {
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
}

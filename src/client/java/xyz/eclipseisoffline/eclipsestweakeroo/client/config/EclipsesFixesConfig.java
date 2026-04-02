package xyz.eclipseisoffline.eclipsestweakeroo.client.config;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBoolean;

import java.util.ArrayList;
import java.util.List;

public class EclipsesFixesConfig {

    private static final List<IConfigBase> CONFIGS = new ArrayList<>();

    public static final ConfigBoolean PISTON_FLEXIBLE_PLACEMENT_FIX = create("pistonFlexiblePlacementFix");
    public static final ConfigBoolean HAND_RESTOCK_CREATIVE_FIX = create("handRestockCreativeFix");
    public static final ConfigBoolean WRITABLE_BOOK_FIX = create("writableBookFormattingFix");
    public static final ConfigBoolean ALTERNATIVE_CONTROL_FIX = create("rightControlFix");

    private static ConfigBoolean create(String name) {
        ConfigBoolean config = new ConfigBoolean(name, false, EclipsesConfigs.getCommentTranslationKey(name));
        CONFIGS.add(config);
        return config;
    }

    public static List<IConfigBase> values() {
        return List.copyOf(CONFIGS);
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.config;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBoolean;

import java.util.ArrayList;
import java.util.List;

public class EclipsesFixesConfig {

    private static final List<IConfigBase> CONFIGS = new ArrayList<>();

    public static final ConfigBoolean PISTON_FLEXIBLE_PLACEMENT_FIX = create("pistonFlexiblePlacementFix", "Fixes inverse rotation placement of pistons, dispensers, droppers and crafters with tweakFlexibleBlockPlacement\n(Allows placing these blocks facing away from you)");
    public static final ConfigBoolean HAND_RESTOCK_CREATIVE_FIX = create("handRestockCreativeFix", "Disables tweakHandRestock in creative mode");
    public static final ConfigBoolean WRITABLE_BOOK_FIX = create("writableBookFix", "Fixes MC-297501, allowing you to use formatting codes in books again,\nand making them display as they would before 1.21.6.\nBreaks the editing cursor when placed mid-text");

    private static ConfigBoolean create(String name, String comment) {
        ConfigBoolean config = new ConfigBoolean(name, false, comment);
        CONFIGS.add(config);
        return config;
    }

    public static List<IConfigBase> values() {
        return List.copyOf(CONFIGS);
    }
}

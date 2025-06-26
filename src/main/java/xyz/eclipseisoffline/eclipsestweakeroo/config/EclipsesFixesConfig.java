package xyz.eclipseisoffline.eclipsestweakeroo.config;

import fi.dy.masa.malilib.config.options.ConfigBoolean;

public class EclipsesFixesConfig {

    public static ConfigBoolean PISTON_FLEXIBLE_PLACEMENT_FIX = new ConfigBoolean(
            "pistonFlexiblePlacementFix", false,
            "Fixes inverse rotation placement of pistons, dispensers, droppers and crafters with tweakFlexibleBlockPlacement\n(Allows placing these blocks facing away from you)");
    public static ConfigBoolean HAND_RESTOCK_CREATIVE_FIX = new ConfigBoolean(
            "handRestockCreativeFix", false,
            "Disables tweakHandRestock in creative mode");
}

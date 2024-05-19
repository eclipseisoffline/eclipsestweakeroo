package xyz.eclipseisoffline.eclipsestweakeroo.config;

import fi.dy.masa.malilib.config.options.ConfigBoolean;

public class AdditionalFixesConfig {

    public static ConfigBoolean PISTON_FLEXIBLE_PLACEMENT_FIX = new ConfigBoolean(
            "pistonFlexiblePlacementFix", false,
            "Fixes inverse rotation placement of pistons, dispensers and droppers with tweakFlexibleBlockPlacement\n(Allows placing these blocks facing away from you)");
}

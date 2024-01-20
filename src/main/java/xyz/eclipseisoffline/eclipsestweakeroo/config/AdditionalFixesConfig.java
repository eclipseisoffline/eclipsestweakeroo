package xyz.eclipseisoffline.eclipsestweakeroo.config;

import fi.dy.masa.malilib.config.options.ConfigBoolean;

public class AdditionalFixesConfig {
    public static ConfigBoolean GAMMA_OVERRIDE_FIX = new ConfigBoolean("gammaOverrideFix", false,
            "Fixes gamma override not applying when starting the game");
}

package xyz.eclipseisoffline.eclipsestweakeroo.config;

import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;

public class AdditionalDisableConfig {
    public static final ConfigBooleanHotkeyed DISABLE_ENTITY_COLLISIONS = new ConfigBooleanHotkeyed("disableEntityCollisions", false, "", "Disables entity collisions");
    public static final ConfigBooleanHotkeyed DISABLE_TAB_HEADER = new ConfigBooleanHotkeyed("disableTabHeader", false, "", "Hides the header in the player list");
    public static final ConfigBooleanHotkeyed DISABLE_TAB_FOOTER = new ConfigBooleanHotkeyed("disableTabFooter", false, "", "Hides the footer in the player list");
}

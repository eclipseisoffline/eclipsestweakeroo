package xyz.eclipseisoffline.eclipsestweakeroo.config;

import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;

public class AdditionalDisableConfig {
    public static final ConfigBooleanHotkeyed DISABLE_ENTITY_COLLISIONS = new ConfigBooleanHotkeyed("disableEntityCollisions", false, "", "Disables entity collisions", "Disable entity collisions");
    public static final ConfigBooleanHotkeyed DISABLE_TAB_HEADER = new ConfigBooleanHotkeyed("disableTabHeader", false, "", "Hides the header in the player list", "Disable tab header");
    public static final ConfigBooleanHotkeyed DISABLE_TAB_FOOTER = new ConfigBooleanHotkeyed("disableTabFooter", false, "", "Hides the footer in the player list", "Disable tab footer");
    public static final ConfigBooleanHotkeyed DISABLE_DISPLAY_NAME = new ConfigBooleanHotkeyed("disableDisplayNames", false, "", "Disables display names in the player list and chat", "Disable display names");
}

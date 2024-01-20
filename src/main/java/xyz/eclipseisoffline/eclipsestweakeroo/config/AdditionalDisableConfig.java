package xyz.eclipseisoffline.eclipsestweakeroo.config;

import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;

public class AdditionalDisableConfig {

    public static final ConfigBooleanHotkeyed DISABLE_ENTITY_COLLISIONS = new ConfigBooleanHotkeyed(
            "disableEntityCollisions", false, "", "Disables entity collisions",
            "Disable entity collisions");
    public static final ConfigBooleanHotkeyed DISABLE_KNOCKBACK = new ConfigBooleanHotkeyed(
            "disableKnockback", false, "", "Disables taking knockback", "Disable knockback");
    public static final ConfigBooleanHotkeyed DISABLE_FOG_MODIFIER = new ConfigBooleanHotkeyed(
            "disableFogModifiers",
            false, "", "Disables all fog modifiers (darkness, blindness, water, etc..)",
            "Disable fog modifiers");
    public static final ConfigBooleanHotkeyed DISABLE_EQUIPMENT_RESTRICTION = new ConfigBooleanHotkeyed(
            "disableArmorRestriction",
            false, "", "Disables the restriction armor slots have. May not work on servers",
            "Disable armor slot restriction");
    public static final ConfigBooleanHotkeyed DISABLE_BINDING_CURSE = new ConfigBooleanHotkeyed(
            "disableBindingCurse",
            false, "", "Disables curse of binding. May not work on servers",
            "Disable curse of binding");
}

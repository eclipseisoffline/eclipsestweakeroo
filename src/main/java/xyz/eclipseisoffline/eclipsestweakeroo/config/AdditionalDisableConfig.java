package xyz.eclipseisoffline.eclipsestweakeroo.config;

import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.tweakeroo.config.ConfigBooleanClient;

public class AdditionalDisableConfig {

    public static final ConfigBooleanHotkeyed DISABLE_ENTITY_COLLISIONS = new ConfigBooleanHotkeyed(
            "disableEntityCollisions", false, "", "Disables entity collisions",
            "Disable entity collisions");
    public static final ConfigBooleanHotkeyed DISABLE_KNOCKBACK = new ConfigBooleanHotkeyed(
            "disableKnockback", false, "", "Disables taking knockback",
            "Disable knockback");
    public static final ConfigBooleanHotkeyed DISABLE_FOG_MODIFIER = new ConfigBooleanHotkeyed(
            "disableFogModifiers",
            false, "", "Disables all fog modifiers (darkness, blindness, water, etc..)",
            "Disable fog modifiers");
    public static final ConfigBooleanHotkeyed DISABLE_EQUIPMENT_RESTRICTION = new ConfigBooleanClient(
            "disableArmorRestriction",
            false, "", "Disables the restriction armor slots have",
            "Disable armor slot restriction");
    public static final ConfigBooleanHotkeyed DISABLE_BINDING_CURSE = new ConfigBooleanClient(
            "disableBindingCurse",
            false, "", "Disables curse of binding",
            "Disable curse of binding");
    public static final ConfigBooleanHotkeyed DISABLE_ITEM_COOLDOWN = new ConfigBooleanClient(
            "disableItemCooldown", false, "",
            "Disables all item cooldowns (for items such as ender pearls, chorus fruits, etc.)");
    public static final ConfigBooleanHotkeyed DISABLE_ILLEGAL_CHARACTER_CHECK = new ConfigBooleanClient(
            "disableIllegalCharacterCheck", false, "",
            "Disables the check for section characters in all text boxes");
    public static final ConfigBooleanHotkeyed DISABLE_BOW_DRAW_TIME = new ConfigBooleanClient(
            "disableBowDrawTime", false, "", "Disables the draw time for (cross)bows");
    public static final ConfigBooleanHotkeyed DISABLE_WORLD_BORDER = new ConfigBooleanClient(
            "disableWorldBorder", false, "", "Removes all world border restrictions");
}

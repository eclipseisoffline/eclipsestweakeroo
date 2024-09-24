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
    public static final ConfigBooleanHotkeyed DISABLE_WORLD_BORDER = new ConfigBooleanHotkeyed(
            "disableWorldBorder", false, "", "Removes all world border restrictions");
    public static final ConfigBooleanHotkeyed DISABLE_BED_EXPLOSION = new ConfigBooleanHotkeyed(
            "disableBedExplosions", false, "",
            "Disables bed explosions in dimensions where beds explode, by cancelling bed clicks.");
    public static final ConfigBooleanHotkeyed DISABLE_ALLAY_USE = new ConfigBooleanHotkeyed(
            "disableAllayItemUse", false, "",
            "Disables giving items to allays");
    public static final ConfigBooleanHotkeyed DISABLE_OFFHAND_USE = new ConfigBooleanHotkeyed(
            "disableOffhandUse", false, "", "Disables using items in your offhand");
    public static final ConfigBooleanHotkeyed DISABLE_HORSE_JUMP_CHARGE = new ConfigBooleanHotkeyed(
            "disableHorseJumpCharge", false, "",
            "Disables (always maximises) the jump charge and cooldown on horses and similar entities");
    public static final ConfigBooleanHotkeyed DISABLE_OVERLAY_RENDER = new ConfigBooleanHotkeyed(
            "disableOverlayRender", false, "",
            "Disables rendering the vignette, spyglass, carved pumpkin, powder snow, and other overlays");
    public static ConfigBooleanHotkeyed DISABLE_ALL_NAMES_IN_F1 = new ConfigBooleanHotkeyed(
            "disableAllNamesInF1", false, "",
            "Disables rendering all name tags in F1 (vanilla Minecraft still renders the names of entities in teams)");
}

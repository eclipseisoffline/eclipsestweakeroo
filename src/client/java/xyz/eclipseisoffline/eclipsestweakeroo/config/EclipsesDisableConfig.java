package xyz.eclipseisoffline.eclipsestweakeroo.config;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.tweakeroo.config.ConfigBooleanClient;

import java.util.ArrayList;
import java.util.List;

public class EclipsesDisableConfig {

    private static final List<IConfigBase> CONFIGS = new ArrayList<>();

    public static final ConfigBooleanHotkeyed DISABLE_ENTITY_COLLISIONS = create("disableEntityCollisions", "Disables entity collisions");
    public static final ConfigBooleanHotkeyed DISABLE_KNOCKBACK = create("disableKnockback", "Disables taking knockback");
    public static final ConfigBooleanHotkeyed DISABLE_FOG_MODIFIER = create("disableFogModifiers", "Disables all fog modifiers (darkness, blindness, water, etc..)");
    public static final ConfigBooleanHotkeyed DISABLE_EQUIPMENT_RESTRICTION = createClient("disableArmorRestriction", "Disables the restriction armor slots have");
    public static final ConfigBooleanHotkeyed DISABLE_BINDING_CURSE = createClient("disableBindingCurse", "Disables curse of binding");
    public static final ConfigBooleanHotkeyed DISABLE_ITEM_COOLDOWN = createClient("disableItemCooldown", "Disables all item cooldowns (for items such as ender pearls, chorus fruits, etc.)");
    public static final ConfigBooleanHotkeyed DISABLE_ILLEGAL_CHARACTER_CHECK = createClient("disableIllegalCharacterCheck", "Disables the check for section characters in all text boxes");
    public static final ConfigBooleanHotkeyed DISABLE_BOW_DRAW_TIME = createClient("disableBowDrawTime", "Disables the draw time for (cross)bows");
    public static final ConfigBooleanHotkeyed DISABLE_WORLD_BORDER = create("disableWorldBorder", "Removes all world border restrictions");
    public static final ConfigBooleanHotkeyed DISABLE_BED_EXPLOSION = create("disableBedExplosions", "Disables bed explosions in dimensions where beds explode, by cancelling bed clicks.");
    public static final ConfigBooleanHotkeyed DISABLE_ALLAY_USE = create("disableAllayItemUse", "Disables giving items to allays");
    public static final ConfigBooleanHotkeyed DISABLE_OFFHAND_USE = create("disableOffhandUse", "Disables using items in your offhand");
    public static final ConfigBooleanHotkeyed DISABLE_HORSE_JUMP_CHARGE = create("disableHorseJumpCharge", "Disables (always maximises) the jump charge and cooldown on horses and similar entities");
    public static final ConfigBooleanHotkeyed DISABLE_OVERLAY_RENDER = create("disableOverlayRender", "Disables rendering the vignette, spyglass, carved pumpkin, powder snow, and other overlays");
    public static final ConfigBooleanHotkeyed DISABLE_ALL_NAMES_IN_F1 = create("disableAllNamesInF1", "Disables rendering all name tags in F1 (vanilla Minecraft still renders the names of entities in teams)");
    public static final ConfigBooleanHotkeyed DISABLE_BLOCK_USE = create("disableBlockUse", "Disables using items on blocks");
    public static final ConfigBooleanHotkeyed DISABLE_USE_ITEM_SLOWDOWN = create("disableUseItemSlowdown", "Disables slowing down when using items, like eating food");
    public static final ConfigBooleanHotkeyed DISABLE_SWIFT_SNEAK = create("disableSwiftSneak", "Disables sneak speed modifiers like swift sneak,\nbecause sometimes slow sneaking is nice");
    public static final ConfigBooleanHotkeyed DISABLE_JUMP_DELAY = create("disableJumpDelay", "Disables the 10-tick delay between jumps");
    public static final ConfigBooleanHotkeyed DISABLE_BOOK_LINE_LIMIT = create("disableBookLineLimit", "Disables the line limit of books, allowing you to write until the full limit of 1024 characters");

    private static ConfigBooleanClient createClient(String name, String comment) {
        ConfigBooleanClient config = new ConfigBooleanClient(name, false, "", comment);
        CONFIGS.add(config);
        return config;
    }

    private static ConfigBooleanHotkeyed create(String name, String comment) {
        ConfigBooleanHotkeyed config = new ConfigBooleanHotkeyed(name, false, "", comment);
        CONFIGS.add(config);
        return config;
    }

    public static List<IConfigBase> values() {
        return List.copyOf(CONFIGS);
    }

    public static List<IHotkeyTogglable> hotkeys() {
        return CONFIGS.stream().map(config -> (IHotkeyTogglable) config).toList();
    }
}

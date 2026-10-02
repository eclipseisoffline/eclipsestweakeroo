package xyz.eclipseisoffline.eclipsestweakeroo.client.config;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;

import java.util.ArrayList;
import java.util.List;

public class EclipsesDisableConfig {

    private static final List<IConfigBase> CONFIGS = new ArrayList<>();

    public static final ConfigBooleanHotkeyed DISABLE_ENTITY_COLLISIONS = createWithServerSideOptIn("disableEntityCollisions");
    public static final ConfigBooleanHotkeyed DISABLE_KNOCKBACK = createWithServerSideOptIn("disableKnockback");
    public static final ConfigBooleanHotkeyed DISABLE_FOG_MODIFIER = create("disableFogModifiers");
    public static final ConfigBooleanHotkeyed DISABLE_EQUIPMENT_RESTRICTION = createClient("disableArmorRestriction");
    public static final ConfigBooleanHotkeyed DISABLE_BINDING_CURSE = createClient("disableBindingCurse");
    public static final ConfigBooleanHotkeyed DISABLE_ITEM_COOLDOWN = createClient("disableItemCooldown");
    public static final ConfigBooleanHotkeyed DISABLE_ILLEGAL_CHARACTER_CHECK = createClient("disableIllegalCharacterCheck");
    public static final ConfigBooleanHotkeyed DISABLE_BOW_DRAW_TIME = createClient("disableBowDrawTime");
    public static final ConfigBooleanHotkeyed DISABLE_WORLD_BORDER = create("disableWorldBorder");
    public static final ConfigBooleanHotkeyed DISABLE_BED_EXPLOSION = create("disableBedExplosions");
    public static final ConfigBooleanHotkeyed DISABLE_ALLAY_USE = create("disableAllayItemUse");
    public static final ConfigBooleanHotkeyed DISABLE_OFFHAND_USE = create("disableOffhandUse");
    public static final ConfigBooleanHotkeyed DISABLE_HORSE_JUMP_CHARGE = createWithServerSideOptIn("disableHorseJumpCharge");
    public static final ConfigBooleanHotkeyed DISABLE_OVERLAY_RENDER = create("disableOverlayRender");
    public static final ConfigBooleanHotkeyed DISABLE_ALL_NAMES_IN_F1 = create("disableAllNamesInF1");
    public static final ConfigBooleanHotkeyed DISABLE_BLOCK_USE = create("disableBlockUse");
    public static final ConfigBooleanHotkeyed DISABLE_USE_ITEM_SLOWDOWN = createWithServerSideOptIn("disableUseItemSlowdown");
    public static final ConfigBooleanHotkeyed DISABLE_SWIFT_SNEAK = create("disableSwiftSneak");
    public static final ConfigBooleanHotkeyed DISABLE_JUMP_DELAY = createWithServerSideOptIn("disableJumpDelay");
    public static final ConfigBooleanHotkeyed DISABLE_BOOK_LINE_LIMIT = create("disableBookLineLimit");
    public static final ConfigBooleanHotkeyed DISABLE_BABY_FEEDING = create("disableBabyFeeding");
    public static final ConfigBooleanHotkeyed DISABLE_END_FLASHES = create("disableEndFlashes");

    private static ClientConfigOption createClient(String name) {
        ClientConfigOption config = new ClientConfigOption(name, false, "");
        CONFIGS.add(config);
        return config;
    }

    private static ConfigBooleanHotkeyed createWithServerSideOptIn(String name) {
        ServerSideOptInConfigOption config = new ServerSideOptInConfigOption(name, false, "");
        CONFIGS.add(config);
        return config;
    }

    private static ConfigBooleanHotkeyed create(String name) {
        ConfigBooleanHotkeyed config = new ConfigBooleanHotkeyed(name, false, "", EclipsesConfigs.getCommentTranslationKey(name),
                name, EclipsesConfigs.getNameTranslationKey(name));
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

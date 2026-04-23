package xyz.eclipseisoffline.eclipsestweakeroo.client.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigDouble;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import fi.dy.masa.malilib.config.options.ConfigOptionList;
import fi.dy.masa.malilib.config.options.ConfigStringList;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class EclipsesGenericConfig {

    private static final List<IConfigBase> CONFIGS = new ArrayList<>();

    public static final ConfigBoolean SERVER_SIDE_DISABLED_MESSAGE = createBoolean("serverDisabledMessage", true);
    public static final ConfigBoolean HAND_RESTOCK_UNSTACKABLE = createBoolean("handRestockUnstackable", true);
    public static final ConfigBoolean TWEAK_PLAYER_LIST_HEADER = createBoolean("playerListHideHeader");
    public static final ConfigBoolean TWEAK_PLAYER_LIST_FOOTER = createBoolean("playerListHideFooter");
    public static final ConfigBoolean TWEAK_PLAYER_LIST_PING = createBoolean("playerListHidePing");
    public static final ConfigBoolean TWEAK_PLAYER_LIST_OBJECTIVE = createBoolean("playerListHideObjective");
    public static final ConfigBoolean TWEAK_PLAYER_LIST_NAMES = createBoolean("playerListNames", true);
    public static final ConfigBoolean TWEAK_PLAYER_LIST_BOSSBAR = createBoolean("playerListBelowBossbar", false);
    public static final ConfigOptionList TWEAK_PLAYER_LIST_ORDER = createOptionList("playerListOrder", PlayerListOrder.DEFAULT);
    public static final ConfigDouble TWEAK_SLIPPERY_SLIPPERINESS = createDouble("slipperiness", Blocks.ICE.getFriction(), 0.6, 1.15);
    public static final ConfigBoolean TWEAK_SLIPPERY_VEHICLES = createBoolean("slipperinessAffectVehicles");
    public static final ConfigStringList FANCY_NAME_ELEMENTS = createStringList("fancyNameElements", ImmutableList.of("{name}", "{gamemode}", "{ping}", "{health}"));
    public static final ConfigDouble TWEAK_JUMP_VELOCITY = createDouble("jumpVelocity", 1.15, 0.5, 5.0);
    public static final ConfigBoolean TWEAK_DURABILITY_PREVENT_USE = createBoolean("durabilityCheckPreventUse");
    public static final ConfigInteger DURABILITY_WARNING_COOLDOWN = createInteger("durabilityWarningCooldown", 300, 10, 900);
    public static final ConfigBoolean COMMAND_ONLY_ADULT_PETS = createBoolean("commandOnlyAdultPets");
    public static final ConfigBoolean ATTACK_PLACEHOLDER_CRITICAL = createBoolean("attackPlaceholderShowCritical", true);
    public static final ConfigInteger RECONNECT_TIME = createInteger("autoReconnectTime", 5000, 100, 15000);
    public static final ConfigBoolean PLAYER_ADD_REMOVE_NOTIFICATION = createBoolean("playerAddRemoveNotification", true);
    public static final ConfigBoolean PLAYER_GAMEMODE_NOTIFICATION = createBoolean("playerGamemodeNotification", true);
    public static final ConfigBoolean PLAYER_LISTED_NOTIFICATION = createBoolean("playerListedNotification", true);
    public static final ConfigBoolean PLAYER_DISPLAY_NAME_NOTIFICATION = createBoolean("playerDisplayNameNotification", true);
    public static final ConfigInteger DURABILITY_PREVENT_USE_THRESHOLD = createInteger("durabilityCheckPreventUseThreshold", 10, 1, 50);
    public static final ConfigBoolean TWEAK_BOAT_SPIDER = createBoolean("spiderBoat");
    public static final ConfigBoolean TWEAK_BOAT_PLAYER_YAW = createBoolean("boatPlayerYaw");
    public static final ConfigInteger TWEAK_NUMBER_HUD_HEALTH_WARNING_THRESHOLD = createInteger("healthWarningThreshold", 5, -1, 20);
    public static final ConfigInteger TWEAK_NUMBER_HUD_HUNGER_WARNING_THRESHOLD = createInteger("hungerWarningThreshold", 15, -1, 20);
    public static final ConfigInteger TWEAK_NUMBER_HUD_AIR_WARNING_THRESHOLD = createInteger("airWarningThreshold", 75, -21, 300);
    public static final ConfigBoolean TWEAK_NUMBER_HUD_SHOW_DURABILITY_WARNING = createBoolean("numberHudDurabilityWarning", true);
    public static final ConfigBoolean DISABLE_EXPLOSION_KNOCKBACK = createBoolean("disableExplosionKnockback");
    public static final ConfigDouble TWEAK_GRAVITY_OVERRIDE = createDouble("gravityOverride", 0.08, -0.5, 0.5);
    public static final ConfigDouble TWEAK_STEP_HEIGHT_OVERRIDE = createDouble("stepHeightOverride", 0.6, 0.0, 1.5);
    public static final ConfigBoolean FAKE_SNEAKING_LADDER = createBoolean("fakeSneakingLadder");
    public static final ConfigBoolean PERMANENT_SNEAK_FREE_CAMERA = createBoolean("permanentSneakFreeCamera");
    public static final ConfigBoolean HAPPY_GHAST_CREATIVE_FLIGHT = createBoolean("happyGhastCreativeFlight");
    public static final ConfigBoolean HIDE_HAPPY_GHAST = createBoolean("hideHappyGhast");
    public static final ConfigBoolean NO_HAPPY_GHAST_ROTATION = createBoolean("noHappyGhastRotation");
    public static final ConfigDouble HAPPY_GHAST_ROTATION_LERP_SPEED = createDouble("happyGhastRotationLerpSpeed", 0.08, 0.01, 1.0);
    public static final ConfigBoolean MUSIC_TOAST_MUSIC = createBoolean("musicToastMusic", true);
    public static final ConfigBoolean MUSIC_TOAST_RECORDS = createBoolean("musicToastRecords", true);
    public static final ConfigBoolean MUSIC_TOAST_PAUSE_MENU = createBoolean("musicToastPauseMenu", true);
    public static final ConfigBoolean TOOL_SWITCH_BACK = createBoolean("toolSwitchBack");
    public static final ConfigInteger FAST_BLOCK_PLACEMENT_INITIAL_DELAY = createInteger("fastBlockPlacementInitialDelay", 0, 0, 256);

    private static ConfigBoolean createBoolean(String name) {
        return createBoolean(name, false);
    }
    
    private static ConfigBoolean createBoolean(String name, boolean defaultValue) {
        ConfigBoolean config = new ConfigBoolean(name, defaultValue, EclipsesConfigs.getCommentTranslationKey(name));
        CONFIGS.add(config);
        return config;
    }

    private static ConfigOptionList createOptionList(String name, IConfigOptionListEntry defaultEntry) {
        ConfigOptionList config = new ConfigOptionList(name, defaultEntry, EclipsesConfigs.getCommentTranslationKey(name));
        CONFIGS.add(config);
        return config;
    }

    private static ConfigDouble createDouble(String name, double defaultValue, double min, double max) {
        ConfigDouble config = new ConfigDouble(name, defaultValue, min, max, EclipsesConfigs.getCommentTranslationKey(name));
        CONFIGS.add(config);
        return config;
    }

    private static ConfigStringList createStringList(String name, ImmutableList<String> values) {
        ConfigStringList config = new ConfigStringList(name, values, EclipsesConfigs.getCommentTranslationKey(name));
        CONFIGS.add(config);
        return config;
    }

    private static ConfigInteger createInteger(String name, int defaultValue, int min, int max) {
        ConfigInteger config = new ConfigInteger(name, defaultValue, min, max, EclipsesConfigs.getCommentTranslationKey(name));
        CONFIGS.add(config);
        return config;
    }

    public static List<IConfigBase> values() {
        return List.copyOf(CONFIGS);
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.config;

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

    public static final ConfigBoolean HAND_RESTOCK_UNSTACKABLE = createBoolean("handRestockUnstackable", true, "Whether to restock unstackable items");
    public static final ConfigBoolean TWEAK_PLAYER_LIST_HEADER = createBoolean("playerListHideHeader", "Hides the player list header when the player list tweak is enabled");
    public static final ConfigBoolean TWEAK_PLAYER_LIST_FOOTER = createBoolean("playerListHideFooter", "Hides the player list footer when the player list tweak is enabled");
    public static final ConfigBoolean TWEAK_PLAYER_LIST_OBJECTIVE = createBoolean("playerListHideObjective", "Hides the scoreboard objectives in the player list when the player list tweak is enabled");
    public static final ConfigBoolean TWEAK_PLAYER_LIST_NAMES = createBoolean("playerListNames", true, "Changes player names in the player list to use fancy names when the player list tweak is enabled");
    public static final ConfigBoolean TWEAK_PLAYER_LIST_BOSSBAR = createBoolean("playerListBelowBossbar", false,
            "Moves the player list below any bossbars that are rendering\nwhen the player list tweak is enabled");
    public static final ConfigOptionList TWEAK_PLAYER_LIST_ORDER = createOptionList("playerListOrder", PlayerListOrder.DEFAULT,
            "Changes the order of players in the player list when the player list tweak is enabled");
    public static final ConfigDouble TWEAK_SLIPPERY_SLIPPERINESS = createDouble("slipperiness",
            Blocks.ICE.getFriction(), 0.6, 1.15,
            "Defines how slippery every block is when using tweakSlippery");
    public static final ConfigBoolean TWEAK_SLIPPERY_VEHICLES = createBoolean(
            "slipperinessAffectVehicles",
            "When true, tweakSlippery affects vehicles you're riding as well");
    public static final ConfigStringList FANCY_NAME_ELEMENTS = createStringList(
            "fancyNameElements",
            ImmutableList.of("{name}", "{gamemode}", "{ping}", "{health}"),
            "Defines how fancy names look. If a placeholder fails, the element will be omitted. Possible placeholders can be found in the README/Modrinth page");
    public static final ConfigDouble TWEAK_JUMP_VELOCITY = createDouble("jumpVelocity",
            1.15, 0.5, 5.0,
            "Defines the jump velocity override when using tweakJumpVelocity");
    public static final ConfigBoolean TWEAK_DURABILITY_PREVENT_USE = createBoolean("durabilityCheckPreventUse", "Prevents using items when they're about to run out if tweakDurabilityCheck is enabled");
    public static final ConfigInteger DURABILITY_WARNING_COOLDOWN = createInteger("durabilityWarningCooldown",
            300, 10, 900,
            "The time to wait before reshowing a durability warning with tweakDurabilityCheck, in seconds");
    public static final ConfigBoolean COMMAND_ONLY_ADULT_PETS = createBoolean("commandOnlyAdultPets", "Target only adult pets with sitDownNearbyPets and standUpNearbyPets hotkeys");
    public static final ConfigBoolean ATTACK_PLACEHOLDER_CRITICAL = createBoolean("attackPlaceholderShowCritical", true, "Show critical hit damage values for players\nin the attack fany name placeholder");
    public static final ConfigInteger RECONNECT_TIME = createInteger("autoReconnectTime",
            5000, 100, 15000,
            "The time to wait before reconnecting with tweakAutoReconnect, in milliseconds");
    public static final ConfigBoolean PLAYER_ADD_REMOVE_NOTIFICATION = createBoolean("playerAddRemoveNotification", true, "Notifies player info additions and removals with tweakPlayerInfoNotifications");
    public static final ConfigBoolean PLAYER_GAMEMODE_NOTIFICATION = createBoolean("playerGamemodeNotification", true, "Notifies player info gamemode changes with tweakPlayerInfoNotifications");
    public static final ConfigBoolean PLAYER_LISTED_NOTIFICATION = createBoolean("playerListedNotification", true, "Notifies listed players changes with tweakPlayerInfoNotifications");
    public static final ConfigBoolean PLAYER_DISPLAY_NAME_NOTIFICATION = createBoolean("playerDisplayNameNotification", true, "Notifies player info display name changes with tweakPlayerInfoNotifications");
    public static ConfigInteger DURABILITY_PREVENT_USE_THRESHOLD = new ConfigInteger("durabilityCheckPreventUseThreshold",
            10, 1, 50,
            "The threshold to start preventing the use of items low on durability");
    public static ConfigBoolean TWEAK_BOAT_SPIDER = createBoolean("spiderBoat", "Boats can jump infinitely high when tweakBoats is enabled");
    public static ConfigBoolean TWEAK_BOAT_PLAYER_YAW = createBoolean("boatPlayerYaw", "Boats follow the yaw of the controlling player when tweakBoats is enabled");
    public static ConfigInteger TWEAK_NUMBER_HUD_HEALTH_WARNING_THRESHOLD = new ConfigInteger("healthWarningThreshold",
            5, -1, 20,
            "Determines when to start flashing the HP text when tweakNumberHud is used");
    public static ConfigInteger TWEAK_NUMBER_HUD_HUNGER_WARNING_THRESHOLD = new ConfigInteger("hungerWarningThreshold",
            15, -1, 20,
            "Determines when to start flashing the hunger text when tweakNumberHud is used");
    public static ConfigInteger TWEAK_NUMBER_HUD_AIR_WARNING_THRESHOLD = new ConfigInteger("airWarningThreshold",
            75, -21, 300,
            "Determines when to start flashing the air text when tweakNumberHud is used");
    public static ConfigBoolean TWEAK_NUMBER_HUD_SHOW_DURABILITY_WARNING = createBoolean("numberHudDurabilityWarning", true, "Shows durability warnings for equipment slots when tweakNumberHud is used");
    public static ConfigBoolean DISABLE_EXPLOSION_KNOCKBACK = createBoolean("disableExplosionKnockback", "Disables knockback from explosions when disableKnockback is enabled");
    public static final ConfigDouble TWEAK_GRAVITY_OVERRIDE = new ConfigDouble("gravityOverride",
            0.08, -0.5, 0.5,
            "Defines the gravity value when using tweakGravity");
    public static final ConfigDouble TWEAK_STEP_HEIGHT_OVERRIDE = new ConfigDouble("stepHeightOverride",
            0.6, 0.0, 1.5,
            "Defines the step height value when using tweakStepHeight");
    public static final ConfigBoolean FAKE_SNEAKING_LADDER = createBoolean("fakeSneakingLadder", "Allows tweakFakeSneaking to stop you from falling down a ladder");
    public static final ConfigBoolean PERMANENT_SNEAK_FREE_CAMERA = createBoolean("permanentSneakFreeCamera", "Allows tweakPermanentSneak to work while using tweakFreeCamera");

    private static ConfigBoolean createBoolean(String name, String comment) {
        return createBoolean(name, false, comment);
    }
    
    private static ConfigBoolean createBoolean(String name, boolean defaultValue, String comment) {
        ConfigBoolean config = new ConfigBoolean(name, defaultValue, comment);
        CONFIGS.add(config);
        return config;
    }

    private static ConfigOptionList createOptionList(String name, IConfigOptionListEntry defaultEntry, String comment) {
        ConfigOptionList config = new ConfigOptionList(name, defaultEntry, comment);
        CONFIGS.add(config);
        return config;
    }

    private static ConfigDouble createDouble(String name, double defaultValue, double min, double max, String comment) {
        ConfigDouble config = new ConfigDouble(name, defaultValue, min, max, comment);
        CONFIGS.add(config);
        return config;
    }

    private static ConfigStringList createStringList(String name, ImmutableList<String> values, String comment) {
        ConfigStringList config = new ConfigStringList(name, values, comment);
        CONFIGS.add(config);
        return config;
    }

    private static ConfigInteger createInteger(String name, int defaultValue, int min, int max, String comment) {
        ConfigInteger config = new ConfigInteger(name, defaultValue, min, max, comment);
        CONFIGS.add(config);
        return config;
    }

    public static List<IConfigBase> values() {
        return List.copyOf(CONFIGS);
    }
}

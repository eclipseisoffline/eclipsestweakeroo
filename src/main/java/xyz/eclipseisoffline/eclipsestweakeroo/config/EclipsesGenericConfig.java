package xyz.eclipseisoffline.eclipsestweakeroo.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigDouble;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import fi.dy.masa.malilib.config.options.ConfigOptionList;
import fi.dy.masa.malilib.config.options.ConfigStringList;
import net.minecraft.world.level.block.Blocks;

public class EclipsesGenericConfig {

    public static final ConfigBoolean HAND_RESTOCK_UNSTACKABLE = new ConfigBoolean(
            "handRestockUnstackable", true, "Whether to restock unstackable items");
    public static final ConfigBoolean TWEAK_PLAYER_LIST_HEADER = new ConfigBoolean(
            "playerListHideHeader", false,
            "Hides the player list header when the player list tweak is enabled");
    public static final ConfigBoolean TWEAK_PLAYER_LIST_FOOTER = new ConfigBoolean(
            "playerListHideFooter", false,
            "Hides the player list footer when the player list tweak is enabled");
    public static final ConfigBoolean TWEAK_PLAYER_LIST_OBJECTIVE = new ConfigBoolean(
            "playerListHideObjective",
            false,
            "Hides the scoreboard objectives in the player list when the player list tweak is enabled");
    public static final ConfigBoolean TWEAK_PLAYER_LIST_NAMES = new ConfigBoolean("playerListNames",
            true,
            "Changes player names in the player list to use fancy names when the player list tweak is enabled");
    public static final ConfigBoolean TWEAK_PLAYER_LIST_BOSSBAR = new ConfigBoolean(
            "playerListBelowBossbar",
            false, "Moves the player list below any bossbars that are rendering\nwhen the player list tweak is enabled");
    public static final ConfigOptionList TWEAK_PLAYER_LIST_ORDER = new ConfigOptionList(
            "playerListOrder", PlayerListOrder.DEFAULT,
            "Changes the order of players in the player list when the player list tweak is enabled");
    public static final ConfigDouble TWEAK_SLIPPERY_SLIPPERINESS = new ConfigDouble("slipperiness",
            Blocks.ICE.getFriction(), 0.6, 1.15,
            "Defines how slippery every block is when using tweakSlippery.");
    public static final ConfigBoolean TWEAK_SLIPPERY_VEHICLES = new ConfigBoolean(
            "slipperinessAffectVehicles", false,
            "When true, tweakSlippery affects vehicles you're riding as well");
    public static final ConfigStringList FANCY_NAME_ELEMENTS = new ConfigStringList(
            "fancyNameElements",
            ImmutableList.of("{name}", "{gamemode}", "{ping}", "{health}"),
            "Defines how fancy names look. If a placeholder fails, the element will be omitted. Possible placeholders can be found in the README/Modrinth page");
    public static final ConfigDouble TWEAK_JUMP_VELOCITY = new ConfigDouble("jumpVelocity",
            1.15, 0.5, 5.0, "Defines the jump velocity override when using tweakJumpVelocity");
    public static final ConfigBoolean TWEAK_DURABILITY_PREVENT_USE = new ConfigBoolean(
            "durabilityCheckPreventUse",
            false,
            "Prevents using items when they're about to run out if tweakDurabilityCheck is enabled");
    public static final ConfigInteger DURABILITY_WARNING_COOLDOWN = new ConfigInteger(
            "durabilityWarningCooldown", 300,
            10, 900,
            "The time to wait before reshowing a durability warning with tweakDurabilityCheck, in seconds");
    public static final ConfigBoolean COMMAND_ONLY_ADULT_PETS = new ConfigBoolean(
            "commandOnlyAdultPets", false,
            "Target only adult pets with sitDownNearbyPets and standUpNearbyPets hotkeys");
    public static final ConfigBoolean ATTACK_PLACEHOLDER_CRITICAL = new ConfigBoolean(
            "attackPlaceholderShowCritical",
            true,
            "Show critical hit damage values for players\nin the attack fany name placeholder");
    public static final ConfigInteger RECONNECT_TIME = new ConfigInteger("autoReconnectTime", 5000,
            100, 15000,
            "The time to wait before reconnecting with tweakAutoReconnect, in milliseconds");
    public static final ConfigBoolean PLAYER_ADD_REMOVE_NOTIFICATION = new ConfigBoolean(
            "playerAddRemoveNotification", true,
            "Notifies player info additions and removals with tweakPlayerInfoNotifications");
    public static final ConfigBoolean PLAYER_GAMEMODE_NOTIFICATION = new ConfigBoolean(
            "playerGamemodeNotification", true,
            "Notifies player info gamemode changes with tweakPlayerInfoNotifications");
    public static final ConfigBoolean PLAYER_LISTED_NOTIFICATION = new ConfigBoolean(
            "playerListedNotification", true,
            "Notifies listed players changes with tweakPlayerInfoNotifications");
    public static final ConfigBoolean PLAYER_DISPLAY_NAME_NOTIFICATION = new ConfigBoolean(
            "playerDisplayNameNotification", true,
            "Notifies player info display name changes with tweakPlayerInfoNotifications");
    public static ConfigInteger DURABILITY_PREVENT_USE_THRESHOLD = new ConfigInteger(
            "durabilityCheckPreventUseThreshold", 10, 1, 50,
            "The threshold to start preventing the use of items low on durability");
    public static ConfigBoolean TWEAK_BOAT_SPIDER = new ConfigBoolean("spiderBoat", false,
            "Boats can jump infinitely high when tweakBoats is enabled");
    public static ConfigBoolean TWEAK_BOAT_PLAYER_YAW = new ConfigBoolean("boatPlayerYaw", false,
            "Boats follow the yaw of the controlling player when tweakBoats is enabled");
    public static ConfigInteger TWEAK_NUMBER_HUD_HEALTH_WARNING_THRESHOLD = new ConfigInteger(
            "healthWarningThreshold", 5, -1, 20,
            "Determines when to start flashing the HP text when tweakNumberHud is used");
    public static ConfigInteger TWEAK_NUMBER_HUD_HUNGER_WARNING_THRESHOLD = new ConfigInteger(
            "hungerWarningThreshold", 15, -1, 20,
            "Determines when to start flashing the hunger text when tweakNumberHud is used");
    public static ConfigInteger TWEAK_NUMBER_HUD_AIR_WARNING_THRESHOLD = new ConfigInteger(
            "airWarningThreshold", 75, -21, 300,
            "Determines when to start flashing the air text when tweakNumberHud is used");
    public static ConfigBoolean TWEAK_NUMBER_HUD_SHOW_DURABILITY_WARNING = new ConfigBoolean(
            "numberHudDurabilityWarning", true,
            "Shows durability warnings for equipment slots when tweakNumberHud is used");
    public static ConfigBoolean DISABLE_EXPLOSION_KNOCKBACK = new ConfigBoolean(
            "disableExplosionKnockback", false,
            "Disables knockback from explosions when disableKnockback is enabled");
    public static final ConfigDouble TWEAK_GRAVITY_OVERRIDE = new ConfigDouble(
            "gravityOverride", 0.08, -0.5, 0.5,
            "Defines the gravity value when using tweakGravity");
    public static final ConfigDouble TWEAK_STEP_HEIGHT_OVERRIDE = new ConfigDouble(
            "stepHeightOverride", 0.6, 0.0, 1.5,
            "Defines the step height value when using tweakStepHeight");
    public static final ConfigBoolean FAKE_SNEAKING_LADDER = new ConfigBoolean("fakeSneakingLadder", false,
            "Allows tweakFakeSneaking to stop you from falling down a ladder");
    public static final ConfigBoolean PERMANENT_SNEAK_FREE_CAMERA = new ConfigBoolean("permanentSneakFreeCamera", false,
            "Allows tweakPermanentSneak to work while using tweakFreeCamera");
}

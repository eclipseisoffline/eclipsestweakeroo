package xyz.eclipseisoffline.eclipsestweakeroo.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigDouble;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import fi.dy.masa.malilib.config.options.ConfigStringList;
import net.minecraft.block.Blocks;

public class AdditionalGenericConfig {

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
    public static final ConfigDouble TWEAK_SLIPPERY_SLIPPERINESS = new ConfigDouble("slipperiness",
            Blocks.ICE.getSlipperiness(), 0.6, 1.15,
            "Defines how slippery every block is when using tweakSlippery.");
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
    public static final ConfigInteger DURABILITY_WARNING_COOLDOWN = new ConfigInteger(
            "durabilityWarningCooldown", 300,
            10, 900,
            "The time to wait before reshowing a durability warning with tweakDurabilityCheck, in seconds");
}

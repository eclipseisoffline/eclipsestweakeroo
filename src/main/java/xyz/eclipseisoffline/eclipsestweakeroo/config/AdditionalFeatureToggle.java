package xyz.eclipseisoffline.eclipsestweakeroo.config;

import fi.dy.masa.tweakeroo.config.FeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.mixin.FeatureToggleCreator;

public class AdditionalFeatureToggle {

    public static final FeatureToggle TWEAK_PLAYER_LIST = newToggle("tweakPlayerList", false, false,
            "", "Makes various changes to the player list.\nCan be configured in General",
            "Player list tweak");
    public static final FeatureToggle TWEAK_PLAYER_NAME = newToggle("tweakPlayerNames", false,
            false,
            "", "Uses fancy names for rending the player names",
            "Player names tweak");
    public static final FeatureToggle TWEAK_MOB_NAMES = newToggle("tweakMobNames", false, false,
            "", "Uses fancy names for rendering mob names, and makes them always show",
            "Mob names tweak");
    public static final FeatureToggle TWEAK_CHAT_MESSAGES = newToggle("tweakChatMessages", false,
            false,
            "",
            "Tries to disable all server chat formatting, to make chat messages display vanilla-like",
            "Chat messages tweak");
    public static final FeatureToggle TWEAK_SLIPPERY = newToggle("tweakSlippery", false, false,
            "",
            "Overrides every block's slipperiness. Can be configured in General. Mostly for fun",
            "Slippery tweak");
    public static final FeatureToggle TWEAK_JUMP_VELOCITY = newToggle("tweakJumpVelocity", false,
            false,
            "",
            "Overrides every block's jump velocity. Can be configured in General. Also mostly for fun",
            "Jump velocity tweak");
    public static final FeatureToggle TWEAK_DURABILITY_CHECK = newToggle("tweakDurabilityCheck",
            false,
            false, "",
            "Adds a durability check, which displays a message when\nyour armor or the item you're currently using is low on durability.\nCan also disable using items low on durability by configuring in General",
            "Durability check tweak");
    public static final FeatureToggle TWEAK_STATUS_EFFECT = newToggle("tweakStatusEffectHud", false,
            false, "", "Modifies the status effect HUD to show effect duration.",
            "Status effect HUD tweak");
    public static final FeatureToggle TWEAK_AUTO_RECONNECT = newToggle("tweakAutoReconnect", false,
            false, "",
            "Automatically reconnects on server disconnect", "Auto reconnect tweak");
    public static final FeatureToggle TWEAK_PLAYER_INFO_NOTIFICATIONS = newToggle(
            "tweakPlayerInfoNotifications", false, false, "",
            "Puts notifications in chat for player info changes.\nCan be configured in General",
            "Player info notifications tweak");
    public static final FeatureToggle TWEAK_BOATS = newToggle("tweakBoats", false, false, "",
            "Disables boat drift on turning and allows boats to jump 1 block high", "Boat tweak");

    private static FeatureToggle newToggle(String name, boolean defaultValue, boolean singlePlayer,
            String defaultHotkey, String comment, String prettyName) {
        return FeatureToggleCreator.constructorInvoker(name.toUpperCase(), -1, name, defaultValue,
                singlePlayer, defaultHotkey, comment, prettyName);
    }
}

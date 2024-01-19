package xyz.eclipseisoffline.eclipsestweakeroo.config;

import fi.dy.masa.tweakeroo.config.FeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.mixin.FeatureToggleCreator;

public class AdditionalFeatureToggle {

    public static final FeatureToggle TWEAK_PLAYER_LIST = newToggle("tweakPlayerList", false, false,
            "", "Makes various changes to the player list", "Player list tweak");
    public static final FeatureToggle TWEAK_PLAYER_NAME = newToggle("tweakPlayerNames", false,
            false,
            "", "Changes the way player names are displayed, like with the player list",
            "Player names tweak");
    public static final FeatureToggle TWEAK_MOB_NAMES = newToggle("tweakMobNames", false, false,
            "", "Makes mob names always show, and shows health in mob names", "Mob names tweak");
    public static final FeatureToggle TWEAK_CHAT_MESSAGES = newToggle("tweakChatMessages", false,
            false,
            "",
            "Tries to disable all server chat formatting, to make chat messages display vanilla-like",
            "Chat messages tweak");
    public static final FeatureToggle TWEAK_SLIPPERY = newToggle("tweakSlippery", false, false,
            "",
            "Overrides every block's slipperiness. Can be configured in General. Mostly for fun",
            "Slippery tweak");

    private static FeatureToggle newToggle(String name, boolean defaultValue, boolean singlePlayer,
            String defaultHotkey, String comment, String prettyName) {
        return FeatureToggleCreator.constructorInvoker(name.toUpperCase(), -1, name, defaultValue,
                singlePlayer, defaultHotkey, comment, prettyName);
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.config;

import fi.dy.masa.tweakeroo.config.FeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.mixin.FeatureToggleCreator;

public class AdditionalFeatureToggle {
    public static FeatureToggle TWEAK_PLAYER_LIST = newToggle("tweakPlayerList", false, false,
            "", "Makes various changes to the player list", "Tweak player list");
    public static FeatureToggle TWEAK_PLAYER_NAME = newToggle("tweakPlayerNames", false, false,
            "", "Changes the way player names are displayed, like with the player list", "Tweak player names");

    private static FeatureToggle newToggle(String name, boolean defaultValue, boolean singlePlayer,
            String defaultHotkey, String comment, String prettyName) {
        return FeatureToggleCreator.constructorInvoker(name.toUpperCase(), -1, name, defaultValue,
                singlePlayer, defaultHotkey, comment, prettyName);
    }
}

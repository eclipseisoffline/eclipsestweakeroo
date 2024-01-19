package xyz.eclipseisoffline.eclipsestweakeroo.config;

import fi.dy.masa.malilib.config.options.ConfigBoolean;

public class AdditionalGenericConfig {

    public static final ConfigBoolean HAND_RESTOCK_UNSTACKABLE = new ConfigBoolean(
            "handRestockUnstackable", true, "Whether to restock unstackable items");
    public static final ConfigBoolean TWEAK_PLAYER_LIST_HEADER = new ConfigBoolean(
            "playerListHideHeader", false,
            "Hides the player list header when the player list tweak is enabled");
    public static final ConfigBoolean TWEAK_PLAYER_LIST_FOOTER = new ConfigBoolean(
            "playerListHideFooter", false,
            "Hides the player list footer when the player list tweak is enabled");
    public static final ConfigBoolean TWEAK_PLAYER_LIST_NAMES = new ConfigBoolean("playerListNames",
            true,
            "Changes player names in the player list to include more information when the player list tweak is enabled");
}

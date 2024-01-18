package xyz.eclipseisoffline.eclipsestweakeroo.config;

import fi.dy.masa.malilib.config.options.ConfigBoolean;

public class AdditionalGenericConfig {
    public static final ConfigBoolean HAND_RESTOCK_UNSTACKABLE = new ConfigBoolean("handRestockUnstackable", true, "Whether to restock unstackable items");
}

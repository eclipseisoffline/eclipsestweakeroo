package xyz.eclipseisoffline.eclipsestweakeroo.client.config;

import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.StringUtils;
import xyz.eclipseisoffline.eclipsestweakeroo.client.util.EclipsesTweakerooUtil;

public class ClientConfigOption extends ConfigBooleanHotkeyed {
    private static final String WARNING_TRANSLATION_KEY = "config.eclipsestweakeroo.client_only_warning";

    public ClientConfigOption(String name, boolean defaultValue, String defaultHotkey, String comment) {
        super(name, defaultValue, defaultHotkey, comment);
    }

    @Override
    public String getConfigGuiDisplayName() {
        return GuiBase.TXT_GOLD + super.getConfigGuiDisplayName() + GuiBase.TXT_RST;
    }

    @Override
    public String getComment() {
        return StringUtils.translate(super.getComment()) + "\n" + EclipsesTweakerooUtil.legacyFormat(StringUtils.translate(WARNING_TRANSLATION_KEY), GuiBase.TXT_GOLD);
    }
}

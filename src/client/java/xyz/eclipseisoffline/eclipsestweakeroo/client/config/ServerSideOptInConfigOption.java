package xyz.eclipseisoffline.eclipsestweakeroo.client.config;

import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.StringUtils;
import xyz.eclipseisoffline.eclipsestweakeroo.client.util.EclipsesTweakerooUtil;

public class ServerSideOptInConfigOption extends ConfigBooleanHotkeyed {
    private static final String WARNING_TRANSLATION_KEY = "config.eclipsestweakeroo.server_side_opt_in_warning";

    public ServerSideOptInConfigOption(String name, boolean defaultValue, String defaultHotkey) {
        super(name, defaultValue, defaultHotkey, EclipsesConfigs.getCommentTranslationKey(name), name,
                EclipsesConfigs.getNameTranslationKey(name));
    }

    @Override
    public String getConfigGuiDisplayName() {
        return GuiBase.TXT_RED + super.getConfigGuiDisplayName() + GuiBase.TXT_RST;
    }

    @Override
    public String getComment() {
        return StringUtils.translate(super.getComment()) + "\n" + EclipsesTweakerooUtil.legacyFormat(StringUtils.translate(WARNING_TRANSLATION_KEY), GuiBase.TXT_RED);
    }
}

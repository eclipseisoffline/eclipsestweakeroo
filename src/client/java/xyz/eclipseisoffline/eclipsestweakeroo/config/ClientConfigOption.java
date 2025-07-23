package xyz.eclipseisoffline.eclipsestweakeroo.config;

import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.malilib.gui.GuiBase;

public class ClientConfigOption extends ConfigBooleanHotkeyed {

    public ClientConfigOption(String name, boolean defaultValue, String defaultHotkey, String comment) {
        super(name, defaultValue, defaultHotkey, comment);
    }

    @Override
    public String getConfigGuiDisplayName() {
        return GuiBase.TXT_GOLD + super.getConfigGuiDisplayName() + GuiBase.TXT_RST;
    }

    @Override
    public String getComment() {
        return super.getComment() + "\n" + GuiBase.TXT_GOLD + "Note: This feature works either at all or" + GuiBase.TXT_RST +
                "\n" + GuiBase.TXT_GOLD + "at least fully only in single player" + GuiBase.TXT_RST;
    }
}

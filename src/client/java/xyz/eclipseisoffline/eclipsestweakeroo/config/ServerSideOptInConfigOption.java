package xyz.eclipseisoffline.eclipsestweakeroo.config;

import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.malilib.gui.GuiBase;

public class ServerSideOptInConfigOption extends ConfigBooleanHotkeyed {

    public ServerSideOptInConfigOption(String name, boolean defaultValue, String defaultHotkey, String comment, String prettyName) {
        super(name, defaultValue, defaultHotkey, comment, prettyName);
    }

    public ServerSideOptInConfigOption(String name, boolean defaultValue, String defaultHotkey, String comment) {
        super(name, defaultValue, defaultHotkey, comment);
    }

    @Override
    public String getConfigGuiDisplayName() {
        return GuiBase.TXT_RED + super.getConfigGuiDisplayName() + GuiBase.TXT_RST;
    }

    @Override
    public String getComment() {
        return super.getComment() + "\n" + GuiBase.TXT_RED + "Note: On multiplayer servers, this feature," + GuiBase.TXT_RST +
                "\n" + GuiBase.TXT_RED + "or a subset of it, requires a server-side opt-in" + GuiBase.TXT_RST;
    }
}

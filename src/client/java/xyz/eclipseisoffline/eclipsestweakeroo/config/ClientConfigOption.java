package xyz.eclipseisoffline.eclipsestweakeroo.config;

import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.StringUtils;

import java.util.Objects;

public class ClientConfigOption extends ConfigBooleanHotkeyed {

    public ClientConfigOption(String name, boolean defaultValue, String defaultHotkey, String comment) {
        this(name, defaultValue, defaultHotkey, comment, StringUtils.splitCamelCase(name), name);
    }

    public ClientConfigOption(String name, boolean defaultValue, String defaultHotkey, String comment, String prettyName, String translatedName) {
        super(name, defaultValue, defaultHotkey, comment, prettyName, translatedName);
    }

    @Override
    public String getComment() {
        String comment = StringUtils.getTranslatedOrFallback(Objects.requireNonNull(super.getComment()), super.getComment());
        if (comment == null) {
            return "";
        }

        return comment + "\n" + GuiBase.TXT_GOLD + "Note: This feature works either at all or\nat least fully only in single player" + GuiBase.TXT_RST;
    }

    @Override
    public String getConfigGuiDisplayName() {
        return GuiBase.TXT_GOLD + super.getConfigGuiDisplayName() + GuiBase.TXT_RST;
    }
}

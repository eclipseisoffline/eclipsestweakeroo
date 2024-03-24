package xyz.eclipseisoffline.eclipsestweakeroo.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import fi.dy.masa.tweakeroo.gui.GuiConfigs;

public class ModMenuCompat implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (parent) -> {
            GuiConfigs configGui = new GuiConfigs();
            configGui.setParent(parent);
            return configGui;
        };
    }
}

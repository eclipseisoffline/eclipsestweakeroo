package xyz.eclipseisoffline.eclipsestweakeroo.client.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import xyz.eclipseisoffline.eclipsestweakeroo.client.gui.EclipsesTweakerooConfig;

public class ModMenuCompat implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return EclipsesTweakerooConfig::new;
    }
}

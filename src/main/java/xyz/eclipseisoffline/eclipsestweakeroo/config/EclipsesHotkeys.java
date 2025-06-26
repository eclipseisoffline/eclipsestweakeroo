package xyz.eclipseisoffline.eclipsestweakeroo.config;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import net.minecraft.client.Minecraft;
import xyz.eclipseisoffline.eclipsestweakeroo.gui.EclipsesTweakerooConfig;

import java.util.ArrayList;
import java.util.List;

public class EclipsesHotkeys {

    private static final List<IConfigBase> CONFIGS = new ArrayList<>();

    public static final ConfigHotkey OPEN_CONFIG_GUI = create("openConfigGui", "E,C", "Open the in-game config GUI");

    private static ConfigHotkey create(String name, String defaultHotkey, String comment) {
        ConfigHotkey hotkey = new ConfigHotkey(name, defaultHotkey, comment);
        CONFIGS.add(hotkey);
        return hotkey;
    }

    public static List<IConfigBase> values() {
        return List.copyOf(CONFIGS);
    }

    public static List<IHotkey> hotkeys() {
        return CONFIGS.stream().map(config -> (IHotkey) config).toList();
    }

    public static void bootstrap() {
        OPEN_CONFIG_GUI.getKeybind().setCallback((keyAction, keybind) -> {
            if (keybind == OPEN_CONFIG_GUI.getKeybind()) {
                Minecraft.getInstance().setScreen(new EclipsesTweakerooConfig(Minecraft.getInstance().screen));
                return true;
            }
            return false;
        });
    }
}

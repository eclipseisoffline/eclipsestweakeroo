package xyz.eclipseisoffline.eclipsestweakeroo.config;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import xyz.eclipseisoffline.eclipsestweakeroo.gui.EclipsesTweakerooConfig;

import java.util.ArrayList;
import java.util.List;

public class EclipsesHotkeys {

    private static final List<IConfigBase> CONFIGS = new ArrayList<>();

    public static final ConfigHotkey OPEN_CONFIG_GUI = create("openConfigGui", "C,E", "Open the in-game config GUI");
    public static final ConfigHotkey INSERT_FORMATTING_CODE = createGui("insertFormattingCode", "", "Inserts a formatting code on the open screen");

    private static ConfigHotkey create(String name, String defaultHotkey, String comment) {
        ConfigHotkey hotkey = new ConfigHotkey(name, defaultHotkey, comment);
        CONFIGS.add(hotkey);
        return hotkey;
    }

    private static ConfigHotkey createGui(String name, String defaultHotkey, String comment) {
        ConfigHotkey hotkey = new ConfigHotkey(name, defaultHotkey, comment);
        hotkey.getKeybind().setSettings(KeybindSettings.GUI);
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
        OPEN_CONFIG_GUI.getKeybind().setCallback((action, key) -> {
            if (key == OPEN_CONFIG_GUI.getKeybind()) {
                Minecraft.getInstance().setScreen(new EclipsesTweakerooConfig(Minecraft.getInstance().screen));
                return true;
            }
            return false;
        });
        INSERT_FORMATTING_CODE.getKeybind().setCallback((action, key) -> {
            if (key == INSERT_FORMATTING_CODE.getKeybind()) {
                Screen openScreen = Minecraft.getInstance().screen;
                if (openScreen != null) {
                    openScreen.charTyped(new CharacterEvent(ChatFormatting.PREFIX_CODE, 0));
                }
                return true;
            }
            return false;
        });
    }
}

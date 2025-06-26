package xyz.eclipseisoffline.eclipsestweakeroo.hotkeys;

import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.IKeybindManager;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesHotkeys;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;

import java.util.stream.Stream;

public class EclipsesKeybindProvider implements IKeybindProvider {

    @Override
    public void addKeysToMap(IKeybindManager manager) {
        collectKeybinds().forEach(manager::addKeybindToMap);
    }

    @Override
    public void addHotkeys(IKeybindManager manager) {
        // Only used for currently unused GUIs, so, not doing much here for now
    }

    private static Stream<IKeybind> collectKeybinds() {
        return Stream.concat(Stream.concat(EclipsesTweaksConfig.hotkeys().stream(), EclipsesHotkeys.hotkeys().stream()), EclipsesDisableConfig.hotkeys().stream())
                .map(IHotkey::getKeybind);
    }

    public static void bootstrap() {
        InputEventHandler.getKeybindManager().registerKeybindProvider(new EclipsesKeybindProvider());
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.client.gui;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Util;
import xyz.eclipseisoffline.eclipsestweakeroo.EclipsesTweakeroo;
import xyz.eclipseisoffline.eclipsestweakeroo.client.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.client.config.EclipsesHotkeys;
import xyz.eclipseisoffline.eclipsestweakeroo.client.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.client.config.EclipsesFixesConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.client.config.EclipsesGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.client.config.EclipsesListsConfig;

import java.util.List;
import java.util.Objects;

public class EclipsesTweakerooConfig extends GuiConfigsBase {

    private static ConfigTab tab = ConfigTab.TWEAKS;

    public EclipsesTweakerooConfig(Screen parent) {
        super(10, 50, EclipsesTweakeroo.MOD_ID, parent, EclipsesTweakeroo.MOD_NAME_SHORT + " Configs - " + EclipsesTweakeroo.MOD_VERSION);
        setParent(parent); // super constructor doesn't do anything with parent...
    }

    @Override
    public void initGui() {
        super.initGui();
        clearOptions();

        int buttonX = 10;
        int buttonY = 26;

        for (ConfigTab tab : ConfigTab.values())  {
            buttonX += createButton(buttonX, buttonY, tab);
        }
    }

    private int createButton(int x, int y, ConfigTab tab) {
        ButtonGeneric button = new ButtonGeneric(x, y, -1, 20, tab.getName());
        button.setEnabled(EclipsesTweakerooConfig.tab != tab);
        addButton(button, new TabSwitcher(tab));

        return button.getWidth() + 2;
    }

    @Override
    protected int getConfigWidth() {
        if (tab == ConfigTab.GENERIC) {
            return 120;
        } else if (tab == ConfigTab.FIXES) {
            return 60;
        } else if (tab == ConfigTab.LISTS) {
            return 200;
        }

        return 260;
    }

    @Override
    protected boolean useKeybindSearch() {
        return tab == ConfigTab.TWEAKS
                || tab == ConfigTab.HOTKEYS
                || tab == ConfigTab.DISABLES;
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        List<IConfigBase> configs = switch (tab) {
            case GENERIC -> EclipsesGenericConfig.values();
            case FIXES -> EclipsesFixesConfig.values();
            case LISTS -> EclipsesListsConfig.values();
            case TWEAKS -> EclipsesTweaksConfig.values();
            case HOTKEYS -> EclipsesHotkeys.values();
            case DISABLES -> EclipsesDisableConfig.values();
        };

        return configs.stream().map(ConfigOptionWrapper::new).toList();
    }

    private class TabSwitcher implements IButtonActionListener {
        private final ConfigTab tab;

        private TabSwitcher(ConfigTab tab) {
            this.tab = tab;
        }

        @Override
        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            EclipsesTweakerooConfig.tab = tab;
            reCreateListWidget();
            Objects.requireNonNull(getListWidget()).resetScrollbarPosition();
            initGui();
        }
    }

    private enum ConfigTab {
        GENERIC("generic"),
        FIXES("fixes"),
        LISTS("lists"),
        TWEAKS("tweaks"),
        HOTKEYS("hotkeys"),
        DISABLES("disables");

        private final String descriptionId;

        ConfigTab(String name) {
            this.descriptionId = Util.makeDescriptionId("config_category", EclipsesTweakeroo.getModdedIdentifier(name));
        }

        public String getName() {
            return StringUtils.translate(descriptionId);
        }
    }
}

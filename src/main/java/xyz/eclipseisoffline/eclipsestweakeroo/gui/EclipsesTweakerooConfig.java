package xyz.eclipseisoffline.eclipsestweakeroo.gui;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.tweakeroo.gui.GuiConfigs;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import xyz.eclipseisoffline.eclipsestweakeroo.EclipsesTweakeroo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesHotkeys;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesFixesConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesListsConfig;

import java.util.List;
import java.util.Objects;

public class EclipsesTweakerooConfig extends GuiConfigsBase {

    private static GuiConfigs.ConfigGuiTab tab = GuiConfigs.ConfigGuiTab.TWEAKS;

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

        for (GuiConfigs.ConfigGuiTab tab : GuiConfigs.ConfigGuiTab.values())  {
            buttonX += createButton(buttonX, buttonY, tab);
        }
    }

    private int createButton(int x, int y, GuiConfigs.ConfigGuiTab tab) {
        ButtonGeneric button = new ButtonGeneric(x, y, -1, 20, tab.getDisplayName());
        button.setEnabled(EclipsesTweakerooConfig.tab != tab);
        addButton(button, new TabSwitcher(tab));

        return button.getWidth() + 2;
    }

    @Override
    protected int getConfigWidth() {
        if (tab == GuiConfigs.ConfigGuiTab.GENERIC) {
            return 120;
        } else if (tab == GuiConfigs.ConfigGuiTab.FIXES) {
            return 60;
        } else if (tab == GuiConfigs.ConfigGuiTab.LISTS) {
            return 200;
        }

        return 260;
    }

    @Override
    protected boolean useKeybindSearch() {
        return tab == GuiConfigs.ConfigGuiTab.TWEAKS
                || tab == GuiConfigs.ConfigGuiTab.GENERIC_HOTKEYS
                || tab == GuiConfigs.ConfigGuiTab.DISABLES;
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        List<IConfigBase> configs = switch (tab) {
            case GENERIC -> EclipsesGenericConfig.values();
            case FIXES -> EclipsesFixesConfig.values();
            case LISTS -> EclipsesListsConfig.values();
            case TWEAKS -> EclipsesTweaksConfig.values();
            case GENERIC_HOTKEYS -> EclipsesHotkeys.values();
            case DISABLES -> EclipsesDisableConfig.values();
        };

        return configs.stream().map(ConfigOptionWrapper::new).toList();
    }

    private class TabSwitcher implements IButtonActionListener {
        private final GuiConfigs.ConfigGuiTab tab;

        private TabSwitcher(GuiConfigs.ConfigGuiTab tab) {
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
}

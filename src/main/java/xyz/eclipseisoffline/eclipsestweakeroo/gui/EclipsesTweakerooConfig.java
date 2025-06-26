package xyz.eclipseisoffline.eclipsestweakeroo.gui;

import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.tweakeroo.gui.GuiConfigs;
import net.minecraft.client.gui.screens.Screen;
import xyz.eclipseisoffline.eclipsestweakeroo.EclipsesTweakeroo;

import java.util.List;
import java.util.Objects;

public class EclipsesTweakerooConfig extends GuiConfigsBase {

    private static GuiConfigs.ConfigGuiTab tab = GuiConfigs.ConfigGuiTab.TWEAKS;

    public EclipsesTweakerooConfig(Screen parent) {
        super(10, 50, EclipsesTweakeroo.MOD_ID, parent, "Eclipse's Tweakeroo Configs");
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
    public List<ConfigOptionWrapper> getConfigs() {
        return List.of();
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

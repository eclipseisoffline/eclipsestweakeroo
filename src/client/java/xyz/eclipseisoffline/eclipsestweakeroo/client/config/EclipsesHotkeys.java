package xyz.eclipseisoffline.eclipsestweakeroo.client.config;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ScrollWheelHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.Items;
import xyz.eclipseisoffline.eclipsestweakeroo.client.gui.EclipsesTweakerooConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.client.inventory.MerchantMenuQuickSell;

import java.util.ArrayList;
import java.util.List;

public class EclipsesHotkeys {

    private static final List<IConfigBase> CONFIGS = new ArrayList<>();

    public static final ConfigHotkey OPEN_CONFIG_GUI = create("openConfigGui", "C,E");
    public static final ConfigHotkey INSERT_FORMATTING_CODE = createGui("insertFormattingCode", "");
    public static final ConfigHotkey NEXT_HOTBAR_SLOT = create("nextHotbarSlot", "");
    public static final ConfigHotkey PREVIOUS_HOTBAR_SLOT = create("previousHotbarSlot", "");
    public static final ConfigHotkey TRADE_SELL_ALL = createGui("tradeSellAll", "");
    public static final ConfigHotkey TRADE_BUY_ALL = createGui("tradeBuyAll", "");

    private static ConfigHotkey create(String name, String defaultHotkey) {
        ConfigHotkey hotkey = new ConfigHotkey(name, defaultHotkey, EclipsesConfigs.getCommentTranslationKey(name));
        CONFIGS.add(hotkey);
        return hotkey;
    }

    private static ConfigHotkey createGui(String name, String defaultHotkey) {
        ConfigHotkey hotkey = new ConfigHotkey(name, defaultHotkey, EclipsesConfigs.getCommentTranslationKey(name));
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
        OPEN_CONFIG_GUI.getKeybind().setCallback((_, key) -> {
            if (key == OPEN_CONFIG_GUI.getKeybind()) {
                Minecraft.getInstance().gui.setScreen(new EclipsesTweakerooConfig(Minecraft.getInstance().gui.screen()));
                return true;
            }
            return false;
        });
        INSERT_FORMATTING_CODE.getKeybind().setCallback((_, key) -> {
            if (key == INSERT_FORMATTING_CODE.getKeybind()) {
                Screen openScreen = Minecraft.getInstance().gui.screen();
                if (openScreen != null) {
                    openScreen.charTyped(new CharacterEvent(ChatFormatting.PREFIX_CODE));
                }
                return true;
            }
            return false;
        });
        NEXT_HOTBAR_SLOT.getKeybind().setCallback((_, key) -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (key == NEXT_HOTBAR_SLOT.getKeybind() && player != null) {
                player.getInventory().setSelectedSlot(ScrollWheelHandler.getNextScrollWheelSelection(-1.0,
                        player.getInventory().getSelectedSlot(), Inventory.getSelectionSize()));
                return true;
            }
            return false;
        });
        PREVIOUS_HOTBAR_SLOT.getKeybind().setCallback((_, key) -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (key == PREVIOUS_HOTBAR_SLOT.getKeybind() && player != null) {
                player.getInventory().setSelectedSlot(ScrollWheelHandler.getNextScrollWheelSelection(1.0,
                        player.getInventory().getSelectedSlot(), Inventory.getSelectionSize()));
                return true;
            }
            return false;
        });
        TRADE_SELL_ALL.getKeybind().setCallback((_, _) -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && player.containerMenu instanceof MerchantMenu menu) {
                ((MerchantMenuQuickSell) menu).eclipsestweakeroo$tradeAll(offer -> offer.getResult().is(Items.EMERALD));
            }
            return true;
        });
        TRADE_BUY_ALL.getKeybind().setCallback((_, _) -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && player.containerMenu instanceof MerchantMenu menu) {
                ((MerchantMenuQuickSell) menu).eclipsestweakeroo$tradeAll(offer -> offer.getBaseCostA().is(Items.EMERALD));
            }
            return true;
        });
    }
}

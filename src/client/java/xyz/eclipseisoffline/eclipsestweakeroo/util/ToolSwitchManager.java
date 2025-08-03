package xyz.eclipseisoffline.eclipsestweakeroo.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.ClickType;

public class ToolSwitchManager {
    private static int lastHotbarSlot = -1;
    private static Pair<Integer, Integer> swappedSlots = null;

    public static void cacheHotbarSlot(Minecraft minecraft, int newSlot) {
        assert minecraft.player != null;
        int currentSlot = minecraft.player.getInventory().getSelectedSlot();
        if (newSlot != currentSlot) {
            System.out.println("reverting and caching");
            revertCache(minecraft);
            lastHotbarSlot = currentSlot;
        } else {
            System.out.println("not caching because it's the same");
        }
    }

    public static void cacheSwappedSlots(int inventory, int hotbar) {
        swappedSlots = Pair.of(inventory, hotbar);
    }

    public static void revertCache(Minecraft minecraft) {
        assert minecraft.player != null;
        if (lastHotbarSlot != -1) {
            minecraft.player.getInventory().setSelectedSlot(lastHotbarSlot);
        }
        if (swappedSlots != null) {
            assert minecraft.gameMode != null;
            minecraft.gameMode.handleInventoryMouseClick(minecraft.player.inventoryMenu.containerId, swappedSlots.getFirst(), swappedSlots.getSecond(), ClickType.SWAP, minecraft.player);
        }
        lastHotbarSlot = -1;
        swappedSlots = null;
    }
}

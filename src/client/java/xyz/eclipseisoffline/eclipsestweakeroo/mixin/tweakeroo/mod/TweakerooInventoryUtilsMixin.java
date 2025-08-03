package xyz.eclipseisoffline.eclipsestweakeroo.mixin.tweakeroo.mod;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fi.dy.masa.tweakeroo.util.InventoryUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesFixesConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToolSwitchManager;

@Mixin(InventoryUtils.class)
public abstract class TweakerooInventoryUtilsMixin {

    @WrapOperation(method = "swapToolToHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V"))
    private static void cacheHotbarSlot(Inventory instance, int slot, Operation<Void> original) {
        if (EclipsesGenericConfig.TOOL_SWITCH_BACK.getBooleanValue()) {
            ToolSwitchManager.cacheHotbarSlot(Minecraft.getInstance(), slot);
        }
        original.call(instance, slot);
    }

    @WrapOperation(method = "swapToolToHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;handleInventoryMouseClick(IIILnet/minecraft/world/inventory/ClickType;Lnet/minecraft/world/entity/player/Player;)V"))
    private static void cacheSwappedSlots(MultiPlayerGameMode instance, int containerId, int slotId, int mouseButton, ClickType clickType, Player player, Operation<Void> original) {
        if (EclipsesGenericConfig.TOOL_SWITCH_BACK.getBooleanValue()) {
            ToolSwitchManager.cacheSwappedSlots(slotId, mouseButton);
        }
        original.call(instance, containerId, slotId, mouseButton, clickType, player);
    }

    @Inject(method = "restockNewStackToHand", at = @At("HEAD"), cancellable = true)
    private static void disableUnstackableAndCreativeRestocking(Player player, InteractionHand hand, ItemStack stackReference,
                                                                boolean allowHotbar, CallbackInfo callbackInfo) {
        if ((!EclipsesGenericConfig.HAND_RESTOCK_UNSTACKABLE.getBooleanValue() && stackReference.getMaxStackSize() <= 1)
                || (EclipsesFixesConfig.HAND_RESTOCK_CREATIVE_FIX.getBooleanValue() && player.isCreative())) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "preRestockHand", at = @At("HEAD"), cancellable = true)
    private static void disableCreativeRestocking(Player player, InteractionHand hand, boolean allowHotbar, CallbackInfo callbackInfo) {
        if (EclipsesFixesConfig.HAND_RESTOCK_CREATIVE_FIX.getBooleanValue() && player.isCreative()) {
            callbackInfo.cancel();
        }
    }
}

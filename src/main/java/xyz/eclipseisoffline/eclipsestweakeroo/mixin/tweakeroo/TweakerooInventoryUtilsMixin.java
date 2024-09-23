package xyz.eclipseisoffline.eclipsestweakeroo.mixin.tweakeroo;

import fi.dy.masa.tweakeroo.util.InventoryUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalGenericConfig;

@Mixin(InventoryUtils.class)
public abstract class TweakerooInventoryUtilsMixin {

    @Inject(method = "restockNewStackToHand", at = @At("HEAD"), cancellable = true)
    private static void disableUnstackableRestocking(Player player, InteractionHand hand, ItemStack stackReference, boolean allowHotbar, CallbackInfo callbackInfo) {
        if (!AdditionalGenericConfig.HAND_RESTOCK_UNSTACKABLE.getBooleanValue()
                && stackReference.getMaxStackSize() <= 1) {
            callbackInfo.cancel();
        }
    }
}

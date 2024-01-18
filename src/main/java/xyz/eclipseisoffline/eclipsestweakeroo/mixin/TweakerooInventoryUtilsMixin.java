package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import fi.dy.masa.tweakeroo.util.InventoryUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalGenericConfig;

@Mixin(InventoryUtils.class)
public class TweakerooInventoryUtilsMixin {

    @Inject(method = "fi.dy.masa.tweakeroo.util.InventoryUtils.restockNewStackToHand", at = @At("HEAD"), cancellable = true)
    private static void preRestockHand(PlayerEntity player, Hand hand, ItemStack stackReference, boolean allowHotbar, CallbackInfo callbackInfo) {
        if (!AdditionalGenericConfig.HAND_RESTOCK_UNSTACKABLE.getBooleanValue() && stackReference.getMaxCount() <= 1) {
            callbackInfo.cancel();
        }
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.mixin.tweakeroo;

import fi.dy.masa.tweakeroo.util.InventoryUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesFixesConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesGenericConfig;

@Mixin(InventoryUtils.class)
public abstract class TweakerooInventoryUtilsMixin {

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

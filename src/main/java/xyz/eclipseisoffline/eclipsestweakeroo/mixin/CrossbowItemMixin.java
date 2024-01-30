package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    @Inject(method = "getPullProgress", at = @At("HEAD"), cancellable = true)
    private static void isCharged(int useTicks, ItemStack stack,
            CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (AdditionalDisableConfig.DISABLE_BOW_DRAW_TIME.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(1.0F);
        }
    }
}

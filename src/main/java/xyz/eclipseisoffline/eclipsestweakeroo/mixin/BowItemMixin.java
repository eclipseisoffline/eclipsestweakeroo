package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.item.BowItem;
import net.minecraft.item.RangedWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;

@Mixin(BowItem.class)
public abstract class BowItemMixin extends RangedWeaponItem {

    public BowItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "getPullProgress", at = @At("HEAD"), cancellable = true)
    private static void fullPullProgress(int useTicks,
            CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (AdditionalDisableConfig.DISABLE_BOW_DRAW_TIME.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(1.0F);
        }
    }
}

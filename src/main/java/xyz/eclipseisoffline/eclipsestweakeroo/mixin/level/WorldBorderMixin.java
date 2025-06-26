package xyz.eclipseisoffline.eclipsestweakeroo.mixin.level;

import net.minecraft.world.level.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;

@Mixin(WorldBorder.class)
public abstract class WorldBorderMixin {

    @Inject(method = "getMinX", at = @At("HEAD"), cancellable = true)
    public void disableWestBound(CallbackInfoReturnable<Double> callbackInfoReturnable) {
        if (EclipsesDisableConfig.DISABLE_WORLD_BORDER.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(-Double.MAX_VALUE);
        }
    }

    @Inject(method = "getMaxX", at = @At("HEAD"), cancellable = true)
    public void disableEastBound(CallbackInfoReturnable<Double> callbackInfoReturnable) {
        if (EclipsesDisableConfig.DISABLE_WORLD_BORDER.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(Double.MAX_VALUE);
        }
    }

    @Inject(method = "getMinZ", at = @At("HEAD"), cancellable = true)
    public void disableNorthBound(CallbackInfoReturnable<Double> callbackInfoReturnable) {
        if (EclipsesDisableConfig.DISABLE_WORLD_BORDER.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(-Double.MAX_VALUE);
        }
    }

    @Inject(method = "getMaxZ", at = @At("HEAD"), cancellable = true)
    public void disableSouthBound(CallbackInfoReturnable<Double> callbackInfoReturnable) {
        if (EclipsesDisableConfig.DISABLE_WORLD_BORDER.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(Double.MAX_VALUE);
        }
    }

    @Inject(method = "getSize", at = @At("HEAD"), cancellable = true)
    public void disableBorder(CallbackInfoReturnable<Double> callbackInfoReturnable) {
        if (EclipsesDisableConfig.DISABLE_WORLD_BORDER.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(Double.MAX_VALUE);
        }
    }
}

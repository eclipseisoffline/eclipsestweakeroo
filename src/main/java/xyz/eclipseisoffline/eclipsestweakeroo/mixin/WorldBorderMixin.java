package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;

@Mixin(WorldBorder.class)
public abstract class WorldBorderMixin {

    @Inject(method = "getBoundWest", at = @At("HEAD"), cancellable = true)
    public void disableWestBound(CallbackInfoReturnable<Double> callbackInfoReturnable) {
        if (AdditionalDisableConfig.DISABLE_WORLD_BORDER.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(-Double.MAX_VALUE);
        }
    }

    @Inject(method = "getBoundEast", at = @At("HEAD"), cancellable = true)
    public void disableEastBound(CallbackInfoReturnable<Double> callbackInfoReturnable) {
        if (AdditionalDisableConfig.DISABLE_WORLD_BORDER.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(Double.MAX_VALUE);
        }
    }

    @Inject(method = "getBoundNorth", at = @At("HEAD"), cancellable = true)
    public void disableNorthBound(CallbackInfoReturnable<Double> callbackInfoReturnable) {
        if (AdditionalDisableConfig.DISABLE_WORLD_BORDER.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(-Double.MAX_VALUE);
        }
    }

    @Inject(method = "getBoundSouth", at = @At("HEAD"), cancellable = true)
    public void disableSouthBound(CallbackInfoReturnable<Double> callbackInfoReturnable) {
        if (AdditionalDisableConfig.DISABLE_WORLD_BORDER.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(Double.MAX_VALUE);
        }
    }

    @Inject(method = "getSize", at = @At("HEAD"), cancellable = true)
    public void disableBorder(CallbackInfoReturnable<Double> callbackInfoReturnable) {
        if (AdditionalDisableConfig.DISABLE_WORLD_BORDER.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(Double.MAX_VALUE);
        }
    }
}

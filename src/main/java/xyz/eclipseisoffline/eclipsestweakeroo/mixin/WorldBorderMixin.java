package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;

@Mixin(WorldBorder.class)
public class WorldBorderMixin {

    @Inject(method = "getBoundWest", at = @At("HEAD"), cancellable = true)
    public void getBoundWest(CallbackInfoReturnable<Double> callbackInfoReturnable) {
        if (AdditionalDisableConfig.DISABLE_WORLD_BORDER.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(-Double.MAX_VALUE);
        }
    }

    @Inject(method = "getBoundEast", at = @At("HEAD"), cancellable = true)
    public void getBoundEast(CallbackInfoReturnable<Double> callbackInfoReturnable) {
        if (AdditionalDisableConfig.DISABLE_WORLD_BORDER.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(Double.MAX_VALUE);
        }
    }

    @Inject(method = "getBoundNorth", at = @At("HEAD"), cancellable = true)
    public void getBoundNorth(CallbackInfoReturnable<Double> callbackInfoReturnable) {
        if (AdditionalDisableConfig.DISABLE_WORLD_BORDER.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(-Double.MAX_VALUE);
        }
    }

    @Inject(method = "getBoundSouth", at = @At("HEAD"), cancellable = true)
    public void getBoundSouth(CallbackInfoReturnable<Double> callbackInfoReturnable) {
        if (AdditionalDisableConfig.DISABLE_WORLD_BORDER.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(Double.MAX_VALUE);
        }
    }

    @Inject(method = "getSize", at = @At("HEAD"), cancellable = true)
    public void getSize(CallbackInfoReturnable<Double> callbackInfoReturnable) {
        if (AdditionalDisableConfig.DISABLE_WORLD_BORDER.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(Double.MAX_VALUE);
        }
    }
}

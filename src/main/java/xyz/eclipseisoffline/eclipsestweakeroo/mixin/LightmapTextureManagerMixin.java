package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;

@Mixin(LightmapTextureManager.class)
public abstract class LightmapTextureManagerMixin implements AutoCloseable {

    @Inject(method = "getDarknessFactor", at = @At("HEAD"), cancellable = true)
    public void disableDarknessEffect(float delta,
            CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (AdditionalDisableConfig.DISABLE_FOG_MODIFIER.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(0.0F);
        }
    }
}

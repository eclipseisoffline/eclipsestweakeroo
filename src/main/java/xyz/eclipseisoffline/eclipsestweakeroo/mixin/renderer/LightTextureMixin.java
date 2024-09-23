package xyz.eclipseisoffline.eclipsestweakeroo.mixin.renderer;

import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;

@Mixin(LightTexture.class)
public abstract class LightTextureMixin implements AutoCloseable {

    @Inject(method = "getDarknessGamma", at = @At("HEAD"), cancellable = true)
    public void disableDarknessEffect(float delta, CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (AdditionalDisableConfig.DISABLE_FOG_MODIFIER.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(0.0F);
        }
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.mixin.renderer;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

@Mixin(LightTexture.class)
public abstract class LightTextureMixin implements AutoCloseable {

    @Inject(method = "calculateDarknessScale", at = @At("HEAD"), cancellable = true)
    public void disableDarknessEffect(LivingEntity entity, float gamma, float partialTick, CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_FOG_MODIFIER)) {
            callbackInfoReturnable.setReturnValue(0.0F);
        }
    }
}

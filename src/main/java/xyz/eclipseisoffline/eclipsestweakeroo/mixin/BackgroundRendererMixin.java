package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.BackgroundRenderer.StatusEffectFogModifier;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {

    @Redirect(method = "applyFog",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;getSubmersionType()Lnet/minecraft/client/render/CameraSubmersionType;"))
    private static CameraSubmersionType disableSubmersionFogModifiers(Camera instance) {
        if (AdditionalDisableConfig.DISABLE_FOG_MODIFIER.getBooleanValue()) {
            return CameraSubmersionType.NONE;
        }

        return instance.getSubmersionType();
    }

    @Inject(method = "getFogModifier", at = @At("HEAD"), cancellable = true)
    private static void disableFogModifiers(Entity entity, float tickDelta,
            CallbackInfoReturnable<StatusEffectFogModifier> callbackInfoReturnable) {
        if (AdditionalDisableConfig.DISABLE_FOG_MODIFIER.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(null);
        }
    }
}

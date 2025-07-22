package xyz.eclipseisoffline.eclipsestweakeroo.mixin.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {

    @WrapOperation(method = "setupFog",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getFluidInCamera()Lnet/minecraft/world/level/material/FogType;"))
    private static FogType disableSubmersionFogModifiers(Camera instance, Operation<FogType> original) {
        if (EclipsesDisableConfig.DISABLE_FOG_MODIFIER.getBooleanValue()) {
            return FogType.NONE;
        }

        return original.call(instance);
    }

    @Inject(method = "getPriorityFogFunction", at = @At("HEAD"), cancellable = true)
    private static void disableMobEffectFogModifiers(Entity entity, float partialTick,
                                                     CallbackInfoReturnable<?> callbackInfoReturnable) {
        if (EclipsesDisableConfig.DISABLE_FOG_MODIFIER.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(null);
        }
    }
}

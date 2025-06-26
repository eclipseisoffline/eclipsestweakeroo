package xyz.eclipseisoffline.eclipsestweakeroo.mixin.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {

    @WrapOperation(method = "computeFogColor",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/fog/environment/FogEnvironment;isApplicable(Lnet/minecraft/world/level/material/FogType;Lnet/minecraft/world/entity/Entity;)Z"))
    public boolean disableFogModifiers(FogEnvironment instance, FogType fogType, Entity entity, Operation<Boolean> original) {
        if (EclipsesDisableConfig.DISABLE_FOG_MODIFIER.getBooleanValue()
                && !(instance instanceof AtmosphericFogEnvironment)) {
            return false;
        }

        return original.call(instance, fogType, entity);
    }

    @Inject(method = "getFogType", at = @At(value = "HEAD"), cancellable = true)
    public void disableFogModifiers(Camera camera, boolean bl, CallbackInfoReturnable<FogType> callbackInfoReturnable) {
        if (EclipsesDisableConfig.DISABLE_FOG_MODIFIER.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(FogType.ATMOSPHERIC);
        }
    }
}

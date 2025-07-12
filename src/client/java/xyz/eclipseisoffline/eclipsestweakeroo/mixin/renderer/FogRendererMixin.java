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
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {

    @WrapOperation(method = {"computeFogColor", "setupFog"},
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/fog/environment/FogEnvironment;isApplicable(Lnet/minecraft/world/level/material/FogType;Lnet/minecraft/world/entity/Entity;)Z"))
    public boolean disableFogModifiers(FogEnvironment instance, FogType fogType, Entity entity, Operation<Boolean> original) {
        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_FOG_MODIFIER)
                && !(instance instanceof AtmosphericFogEnvironment)) {
            return false;
        }

        return original.call(instance, fogType, entity);
    }

    @Inject(method = "getFogType", at = @At(value = "HEAD"), cancellable = true)
    public void disableFogModifiers(Camera camera, boolean bl, CallbackInfoReturnable<FogType> callbackInfoReturnable) {
        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_FOG_MODIFIER)) {
            callbackInfoReturnable.setReturnValue(FogType.ATMOSPHERIC);
        }
    }
}

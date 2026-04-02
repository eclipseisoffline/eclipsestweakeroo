package xyz.eclipseisoffline.eclipsestweakeroo.client.mixin.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.EndFlashState;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.client.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.client.util.ToggleManager;

@Mixin(LightmapRenderStateExtractor.class)
public abstract class LightTextureMixin implements AutoCloseable {

    @WrapOperation(method = "extract", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;endFlashState()Lnet/minecraft/client/renderer/EndFlashState;"))
    public @Nullable EndFlashState noEndFlashes(ClientLevel instance, Operation<EndFlashState> original) {
        if (EclipsesDisableConfig.DISABLE_END_FLASHES.getBooleanValue()) {
            return null;
        }
        return original.call(instance);
    }

    @Inject(method = "calculateDarknessScale", at = @At("HEAD"), cancellable = true)
    public void disableDarknessEffect(LivingEntity entity, float gamma, float partialTick, CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_FOG_MODIFIER)) {
            callbackInfoReturnable.setReturnValue(0.0F);
        }
    }
}

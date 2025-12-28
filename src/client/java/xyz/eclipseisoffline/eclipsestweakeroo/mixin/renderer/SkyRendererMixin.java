package xyz.eclipseisoffline.eclipsestweakeroo.mixin.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.EndFlashState;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.SkyRenderState;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;

@Mixin(SkyRenderer.class)
public abstract class SkyRendererMixin {

    @WrapOperation(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;endFlashState()Lnet/minecraft/client/renderer/EndFlashState;"))
    public @Nullable EndFlashState noEndFlashes(ClientLevel instance, Operation<EndFlashState> original, @Local(argsOnly = true) SkyRenderState state) {
        if (EclipsesDisableConfig.DISABLE_END_FLASHES.getBooleanValue()) {
            state.endFlashIntensity = 0.0F;
            return null;
        }
        return original.call(instance);
    }
}

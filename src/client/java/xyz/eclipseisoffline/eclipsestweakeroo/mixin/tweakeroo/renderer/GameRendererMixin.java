package xyz.eclipseisoffline.eclipsestweakeroo.mixin.tweakeroo.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fi.dy.masa.tweakeroo.config.FeatureToggle;
import fi.dy.masa.tweakeroo.config.Hotkeys;
import fi.dy.masa.tweakeroo.util.CameraUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesFixesConfig;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    /*@WrapOperation(method = {"renderLevel", "extractCamera"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;rotation()Lorg/joml/Quaternionf;"))
    public Quaternionf useElytraCameraRotation(Camera instance, Operation<Quaternionf> original) {
        if (FeatureToggle.TWEAK_ELYTRA_CAMERA.getBooleanValue() && Hotkeys.ELYTRA_CAMERA.getKeybind().isKeybindHeld() && EclipsesFixesConfig.ELYTRA_CAMERA_FIX.getBooleanValue()) {
            return new Quaternionf().rotationYXZ((float)Math.PI - CameraUtils.getCameraYaw() * ((float)Math.PI / 180F), -CameraUtils.getCameraPitch() * ((float)Math.PI / 180F), 0.0F);
        }
        return original.call(instance);
    }*/ // FIXME STILL NECESSARY?
}

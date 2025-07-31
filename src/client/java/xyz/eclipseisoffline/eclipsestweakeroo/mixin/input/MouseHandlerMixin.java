package xyz.eclipseisoffline.eclipsestweakeroo.mixin.input;

import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {

    @ModifyArg(method = "turnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"), index = 0)
    public double invertMouseX(double x) {
        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_INVERT_MOUSE_X)) {
            return -x;
        }
        return x;
    }
}

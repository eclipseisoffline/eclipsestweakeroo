package xyz.eclipseisoffline.eclipsestweakeroo.mixin.input;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesFixesConfig;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardHandlerMixin {

    @WrapOperation(method = "lambda$setup$0", at = @At(value = "NEW", target = "net/minecraft/client/input/KeyEvent"))
    public KeyEvent alternativeControlModifier(int key, int scancode, int modifiers, Operation<KeyEvent> original) {
        if (Minecraft.getInstance().hasControlDown() && EclipsesFixesConfig.ALTERNATIVE_CONTROL_FIX.getBooleanValue()) {
            modifiers |= 0b10;
        }
        return original.call(key, scancode, modifiers);
    }
}

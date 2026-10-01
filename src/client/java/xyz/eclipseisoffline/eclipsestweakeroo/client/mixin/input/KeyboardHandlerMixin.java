package xyz.eclipseisoffline.eclipsestweakeroo.client.mixin.input;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.InputQuirks;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.client.config.EclipsesFixesConfig;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardHandlerMixin {

    @Inject(method = "keyPress", at = @At("HEAD"))
    public void alternativeControlModifier(long handle, int action, KeyEvent event, CallbackInfo callbackInfo,
                                           @Local(argsOnly = true, name = "event") LocalRef<KeyEvent> eventRef) {
        if (Minecraft.getInstance().hasControlDown() && EclipsesFixesConfig.ALTERNATIVE_CONTROL_FIX.getBooleanValue()) {
            eventRef.set(new KeyEvent(event.key(), event.keycode(), event.modifiers() | InputQuirks.EDIT_SHORTCUT_KEY_MODIFIER));
        }
    }
}

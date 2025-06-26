package xyz.eclipseisoffline.eclipsestweakeroo.mixin.tweakeroo;

import fi.dy.masa.malilib.hotkeys.IKeybindManager;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import fi.dy.masa.malilib.hotkeys.IKeyboardInputHandler;
import fi.dy.masa.malilib.hotkeys.IMouseInputHandler;
import fi.dy.masa.tweakeroo.config.FeatureToggle;
import fi.dy.masa.tweakeroo.event.InputHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.util.EclipsesTweakerooUtil;

@Mixin(InputHandler.class)
public abstract class TweakerooInputHandlerMixin implements IKeybindProvider, IKeyboardInputHandler, IMouseInputHandler {

    @Inject(method = "addKeysToMap", at = @At("TAIL"), remap = false)
    public void addKeysToMap(IKeybindManager manager, CallbackInfo callbackInfo) {
        for (FeatureToggle newToggle : EclipsesTweakerooUtil.getDeclaredFeatureToggles(EclipsesFeatureToggle.class)) {
            manager.addKeybindToMap(newToggle.getKeybind());
        }
    }
}

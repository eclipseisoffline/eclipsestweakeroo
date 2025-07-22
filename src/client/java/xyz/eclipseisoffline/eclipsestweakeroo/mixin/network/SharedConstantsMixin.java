package xyz.eclipseisoffline.eclipsestweakeroo.mixin.network;

import net.minecraft.SharedConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

@Mixin(SharedConstants.class)
public abstract class SharedConstantsMixin {

    @Inject(method = "isAllowedChatCharacter", at = @At("HEAD"), cancellable = true)
    private static void allowAllCharacters(char chr,
            CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_ILLEGAL_CHARACTER_CHECK)) {
            callbackInfoReturnable.setReturnValue(true);
        }
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.mixin.network;

import net.minecraft.util.StringUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

@Mixin(StringUtil.class)
public abstract class StringHelperMixin {

    @Inject(method = "isAllowedChatCharacter", at = @At("HEAD"), cancellable = true)
    private static void allowAllCharacters(char chr,
            CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_ILLEGAL_CHARACTER_CHECK)) {
            callbackInfoReturnable.setReturnValue(true);
        }
    }
}

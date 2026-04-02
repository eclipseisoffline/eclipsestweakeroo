package xyz.eclipseisoffline.eclipsestweakeroo.client.mixin.network;

import net.minecraft.util.StringUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.client.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.client.util.ToggleManager;

@Mixin(StringUtil.class)
public abstract class StringUtilMixin {

    @Inject(method = "isAllowedChatCharacter", at = @At("HEAD"), cancellable = true)
    private static void allowAllCharacters(int codePoint, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_ILLEGAL_CHARACTER_CHECK)) {
            callbackInfoReturnable.setReturnValue(true);
        }
    }
}

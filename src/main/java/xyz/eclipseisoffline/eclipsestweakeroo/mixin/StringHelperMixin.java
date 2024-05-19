package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.util.StringHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;

@Mixin(StringHelper.class)
public abstract class StringHelperMixin {

    @Inject(method = "isValidChar", at = @At("HEAD"), cancellable = true)
    private static void allowAllCharacters(char chr,
            CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (AdditionalDisableConfig.DISABLE_ILLEGAL_CHARACTER_CHECK.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(true);
        }
    }
}

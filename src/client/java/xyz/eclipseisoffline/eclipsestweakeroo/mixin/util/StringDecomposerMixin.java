package xyz.eclipseisoffline.eclipsestweakeroo.mixin.util;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.StringDecomposer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

@Mixin(StringDecomposer.class)
public abstract class StringDecomposerMixin {

    @Inject(method = "iterateFormatted(Ljava/lang/String;ILnet/minecraft/network/chat/Style;Lnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;)Z",
            at = @At(value = "CONSTANT", args = "intValue=1", ordinal = 0))
    private static void acceptDebugFormattingCodes(String text, int skip, Style currentStyle, Style defaultStyle, FormattedCharSink sink,
                                                   CallbackInfoReturnable<Boolean> callbackInfoReturnable, @Local(ordinal = 2) int j) {
        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_SHOW_FORMATTING_CODES)) {
            sink.accept(j, Style.EMPTY.withColor(0xAAAAAA), ChatFormatting.PREFIX_CODE);
            if (j + 1 < text.length()) {
                sink.accept(j + 1, Style.EMPTY.withColor(0xAAAAAA), text.charAt(j + 1));
            }
        }
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.client.mixin.util;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.StringDecomposer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.client.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.client.util.ToggleManager;

@Mixin(StringDecomposer.class)
public abstract class StringDecomposerMixin {

    @Inject(method = "iterateFormatted(Ljava/lang/String;ILnet/minecraft/network/chat/Style;Lnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;)Z",
            at = @At(value = "CONSTANT", args = "intValue=1", ordinal = 0))
    private static void acceptDebugFormattingCodes(String text, int skip, Style currentStyle, Style defaultStyle, FormattedCharSink sink,
                                                   CallbackInfoReturnable<Boolean> callbackInfoReturnable, @Local(name = "i") int i) {
        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_SHOW_FORMATTING_CODES)) {
            sink.accept(i, Style.EMPTY.withColor(0xAAAAAA), ChatFormatting.PREFIX_CODE);
            if (i + 1 < text.length()) {
                sink.accept(i + 1, Style.EMPTY.withColor(0xAAAAAA), text.charAt(i + 1));
            }
        }
    }
}

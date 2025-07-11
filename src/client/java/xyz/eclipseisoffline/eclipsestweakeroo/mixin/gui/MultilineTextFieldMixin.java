package xyz.eclipseisoffline.eclipsestweakeroo.mixin.gui;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.components.MultilineTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import xyz.eclipseisoffline.eclipsestweakeroo.gui.components.MultilineTextFieldController;

@Mixin(MultilineTextField.class)
public abstract class MultilineTextFieldMixin implements MultilineTextFieldController {

    @Unique
    private boolean allowIllegalCharacters = false;

    @Override
    public void eclipsestweakeroo$setAllowIllegalCharacters(boolean allowIllegalCharacters) {
        this.allowIllegalCharacters = allowIllegalCharacters;
    }

    @WrapOperation(method = "insertText", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/StringUtil;filterText(Ljava/lang/String;Z)Ljava/lang/String;"))
    public String filterWithoutIllegalFilter(String text, boolean allowLineBreaks, Operation<String> original) {
        if (allowIllegalCharacters) {
            return filterTextKeepIllegals(text, allowLineBreaks);
        }
        return original.call(text, allowLineBreaks);
    }

    @Unique
    private static String filterTextKeepIllegals(String text, boolean allowLineBreaks) {
        StringBuilder builder = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (c == '\n' && !allowLineBreaks) {
                continue;
            }
            builder.append(c);
        }

        return builder.toString();
    }
}

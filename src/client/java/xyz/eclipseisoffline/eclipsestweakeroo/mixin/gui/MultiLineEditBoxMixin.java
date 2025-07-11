package xyz.eclipseisoffline.eclipsestweakeroo.mixin.gui;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractTextAreaWidget;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.components.MultilineTextField;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import xyz.eclipseisoffline.eclipsestweakeroo.gui.components.MultiLineEditBoxAdditions;

import java.util.List;

@Mixin(MultiLineEditBox.class)
public abstract class MultiLineEditBoxMixin extends AbstractTextAreaWidget implements MultiLineEditBoxAdditions {

    @Shadow
    @Final
    private MultilineTextField textField;

    @Shadow
    @Final
    private Font font;

    @Shadow
    @Final
    private int textColor;

    @Shadow
    @Final
    private boolean textShadow;

    public MultiLineEditBoxMixin(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    public MultilineTextField eclipsestweakeroo$getTextField() {
        return textField;
    }

    @WrapOperation(method = "renderContents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/MultilineTextField;iterateLines()Ljava/lang/Iterable;", ordinal = 0))
    public Iterable customTextRenderer(MultilineTextField instance, Operation<Iterable> original, @Local(argsOnly = true) GuiGraphics graphics,
                                       @Local(ordinal = 3) LocalIntRef j, @Local(ordinal = 4) LocalIntRef k) {
        if (eclipsestweakeroo$renderTextFormatting()) {
            List<FormattedCharSequence> pageComponents = font.split(FormattedText.of(textField.value()), ((MultilineTextFieldAccessor) textField).getWidth());

            int startX = getInnerLeft();
            int startY = getInnerTop();

            for (int i = 0; i < pageComponents.size(); i++) {
                FormattedCharSequence sequence = pageComponents.get(i);

                int y = startY + i * 9;
                graphics.drawString(font, sequence, startX, y, textColor, textShadow);
                j.set(startX + font.width(sequence) - 1);
            }
            k.set(startY + (pageComponents.size() - 1) * 9);

            return List.of();
        }
        return original.call(instance);
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.gui.components;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.network.chat.Component;
import xyz.eclipseisoffline.eclipsestweakeroo.mixin.gui.MultiLineEditBoxBuilderAccessor;

public class BookPageEditBox extends MultiLineEditBox implements MultiLineEditBoxAdditions {

    private BookPageEditBox(Font font, int width, int height, Component message, MultiLineEditBoxBuilderAccessor builder) {
        super(font, builder.getX(), builder.getY(), width, height,
                builder.getPlaceholder(), message, builder.getTextColor(), builder.getTextShadow(),
                builder.getCursorColor(), builder.getShowBackground(), builder.getShowDecorations());
        ((MultilineTextFieldController) eclipsestweakeroo$getTextField()).eclipsestweakeroo$setAllowIllegalCharacters(true);
    }

    public BookPageEditBox(Font font, int width, int height, Component message, MultiLineEditBox.Builder builder) {
        this(font, width, height, message, (MultiLineEditBoxBuilderAccessor) builder);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (visible && isFocused()) {
            eclipsestweakeroo$getTextField().insertText(Character.toString(codePoint));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean eclipsestweakeroo$renderTextFormatting() {
        return true;
    }
}

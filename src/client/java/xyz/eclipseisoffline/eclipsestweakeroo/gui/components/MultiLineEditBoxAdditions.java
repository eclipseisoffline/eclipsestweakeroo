package xyz.eclipseisoffline.eclipsestweakeroo.gui.components;

import net.minecraft.client.gui.components.MultilineTextField;

public interface MultiLineEditBoxAdditions {

    default MultilineTextField eclipsestweakeroo$getTextField() {
        throw new AssertionError();
    }

    default boolean eclipsestweakeroo$renderTextFormatting() {
        return false;
    }
}

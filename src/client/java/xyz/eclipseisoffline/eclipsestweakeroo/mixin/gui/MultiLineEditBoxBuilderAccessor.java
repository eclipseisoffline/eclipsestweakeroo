package xyz.eclipseisoffline.eclipsestweakeroo.mixin.gui;

import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MultiLineEditBox.Builder.class)
public interface MultiLineEditBoxBuilderAccessor {

    @Accessor
    int getX();

    @Accessor
    int getY();

    @Accessor
    Component getPlaceholder();

    @Accessor
    int getTextColor();

    @Accessor
    boolean getTextShadow();

    @Accessor
    int getCursorColor();

    @Accessor
    boolean getShowBackground();

    @Accessor
    boolean getShowDecorations();
}

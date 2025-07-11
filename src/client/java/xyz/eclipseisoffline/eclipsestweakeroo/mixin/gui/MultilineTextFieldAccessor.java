package xyz.eclipseisoffline.eclipsestweakeroo.mixin.gui;

import net.minecraft.client.gui.components.MultilineTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MultilineTextField.class)
public interface MultilineTextFieldAccessor {

    @Accessor
    int getWidth();
}

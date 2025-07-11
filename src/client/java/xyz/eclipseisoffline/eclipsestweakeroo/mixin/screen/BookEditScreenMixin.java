package xyz.eclipseisoffline.eclipsestweakeroo.mixin.screen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesFixesConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.gui.components.BookPageEditBox;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

@Mixin(BookEditScreen.class)
public abstract class BookEditScreenMixin extends Screen {

    protected BookEditScreenMixin(Component title) {
        super(title);
    }

    @WrapOperation(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/MultiLineEditBox$Builder;build(Lnet/minecraft/client/gui/Font;IILnet/minecraft/network/chat/Component;)Lnet/minecraft/client/gui/components/MultiLineEditBox;"))
    public MultiLineEditBox buildBookPageEditBox(MultiLineEditBox.Builder instance, Font font, int width, int height, Component message, Operation<MultiLineEditBox> original) {
        if (ToggleManager.enabled(EclipsesFixesConfig.WRITABLE_BOOK_FIX)) {
            return new BookPageEditBox(font, width, height, message, instance);
        }
        return original.call(instance, font, width, height, message);
    }
}

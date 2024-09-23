package xyz.eclipseisoffline.eclipsestweakeroo.mixin.tweakeroo;

import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.tweakeroo.gui.GuiConfigs;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.EclipsesTweakeroo;

@Mixin(GuiConfigs.class)
public abstract class GuiConfigsMixin extends GuiConfigsBase {

    public GuiConfigsMixin(int listX, int listY, String modId, Screen parent, String titleKey, Object... args) {
        super(listX, listY, modId, parent, titleKey, args);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void modifyTitle(CallbackInfo callbackInfo) {
        String version = FabricLoader.getInstance()
                .getModContainer(EclipsesTweakeroo.MOD_ID).orElseThrow()
                .getMetadata().getVersion().getFriendlyString();
        title += " with Eclipse's additions version " + version;
    }
}

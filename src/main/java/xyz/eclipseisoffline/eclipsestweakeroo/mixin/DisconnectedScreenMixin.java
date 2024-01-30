package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget.Builder;
import net.minecraft.client.gui.widget.ButtonWidget.PressAction;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.EclipsesTweakerooUtil;

@Mixin(DisconnectedScreen.class)
public abstract class DisconnectedScreenMixin extends Screen {

    @Unique
    private static final Text TO_MENU_TEXT = Text.translatable("gui.toMenu");

    @Shadow
    @Final
    private Screen parent;
    @Unique
    private Text buttonLabel = TO_MENU_TEXT;
    @Unique
    private int start;
    @Unique
    private boolean registeredAfterRender = false;

    protected DisconnectedScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    protected abstract void init();

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(Screen parent, Text title, Text reason, CallbackInfo callbackInfo) {
        start = EclipsesTweakerooUtil.milliTime();
    }

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;"))
    public Builder createBuilderWithCustomMessage(Text message, PressAction onPress) {
        if (AdditionalFeatureToggle.TWEAK_AUTO_RECONNECT.getBooleanValue()) {
            return ButtonWidget.builder(buttonLabel, onPress);
        }
        return ButtonWidget.builder(message, onPress);
    }

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;dimensions(IIII)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;"))
    public Builder expandButtonWidth(Builder instance, int x, int y, int width, int height) {
        if (AdditionalFeatureToggle.TWEAK_AUTO_RECONNECT.getBooleanValue()) {
            return instance.dimensions(this.width / 2 - 150, y, 300, height);
        }
        return instance.dimensions(x, y, width, height);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void registerAfterRender(CallbackInfo callbackInfo) {
        if (registeredAfterRender) {
            return;
        }
        ScreenEvents.afterRender(this).register(((screen, drawContext, mouseX, mouseY, tickDelta) -> {
            int passed = EclipsesTweakerooUtil.milliTime() - start;
            int wait = AdditionalGenericConfig.RECONNECT_TIME.getIntegerValue();
            if (passed > wait) {
                ConnectScreen.connect(parent, MinecraftClient.getInstance(),
                        EclipsesTweakerooUtil.getLastConnection(),
                        EclipsesTweakerooUtil.getLastConnectionInfo());
            }
            DisconnectedScreenMixin.this.buttonLabel = TO_MENU_TEXT.copy()
                    .append(Text.of(" (reconnecting in " + (wait - passed) + "ms)"));
            clearAndInit();
        }));
        registeredAfterRender = true;
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget.Builder;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
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

    @Shadow
    @Final
    private static Text TO_MENU_TEXT;
    @Shadow
    @Mutable
    @Final
    private Text buttonLabel;
    @Shadow
    @Mutable
    @Final
    private DirectionalLayoutWidget grid;
    @Shadow
    @Final
    private Screen parent;
    @Unique
    private int start;
    @Unique
    private boolean registeredAfterRender = false;

    protected DisconnectedScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    protected abstract void init();

    @Inject(method = "<init>(Lnet/minecraft/client/gui/screen/Screen;Lnet/minecraft/text/Text;Lnet/minecraft/text/Text;Lnet/minecraft/text/Text;)V", at = @At("TAIL"))
    public void constructor(Screen parent, Text title, Text reason, Text buttonLabel,
            CallbackInfo callbackInfo) {
        start = EclipsesTweakerooUtil.milliTime();
    }

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;"))
    public ButtonWidget setWidthAndBuild(Builder instance) {
        if (AdditionalFeatureToggle.TWEAK_AUTO_RECONNECT.getBooleanValue()) {
            return instance.width(300).build();
        }
        return instance.build();
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
                        EclipsesTweakerooUtil.getLastConnectionInfo(),
                        false);
            }
            DisconnectedScreenMixin.this.buttonLabel = TO_MENU_TEXT.copy()
                    .append(Text.of(" (reconnecting in " + (wait - passed) + "ms)"));
            grid = DirectionalLayoutWidget.vertical();
            clearAndInit();
        }));
        registeredAfterRender = true;
    }
}

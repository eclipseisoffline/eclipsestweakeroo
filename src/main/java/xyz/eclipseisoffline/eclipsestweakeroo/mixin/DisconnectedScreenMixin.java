package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
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
    @Unique
    private int start;
    @Shadow
    @Final
    private Screen parent;

    protected DisconnectedScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    protected abstract void init();

    @Inject(method = "<init>(Lnet/minecraft/client/gui/screen/Screen;Lnet/minecraft/text/Text;Lnet/minecraft/text/Text;Lnet/minecraft/text/Text;)V", at = @At("TAIL"))
    public void constructor(Screen parent, Text title, Text reason, Text buttonLabel,
            CallbackInfo callbackInfo) {
        start = (int) (System.nanoTime() * 0.000001);
    }

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;build()Lnet/minecraft/client/gui/widget/ButtonWidget;"))
    public ButtonWidget setWidthAndBuild(Builder instance) {
        if (AdditionalFeatureToggle.TWEAK_AUTO_RECONNECT.getBooleanValue()) {
            return instance.width(300).build();
        }
        return instance.build();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        //noinspection ConstantValue
        if ((Object) this instanceof DisconnectedScreen
                && AdditionalFeatureToggle.TWEAK_AUTO_RECONNECT.getBooleanValue()) {
            int passed = (int) (System.nanoTime() * 0.000001) - start;
            int wait = AdditionalGenericConfig.RECONNECT_TIME.getIntegerValue();
            if (passed > wait) {
                ConnectScreen.connect(parent, MinecraftClient.getInstance(),
                        EclipsesTweakerooUtil.getLastConnection(),
                        EclipsesTweakerooUtil.getLastConnectionInfo(),
                        false);
            }
            buttonLabel = TO_MENU_TEXT.copy()
                    .append(Text.of(" (reconnecting in " + (wait - passed) + "ms)"));
            grid = DirectionalLayoutWidget.vertical();
            clearAndInit();
        }
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.tweakeroo.config.Configs.Disable;
import java.util.ArrayList;
import java.util.List;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.TweakerooConfigMixinHelper;

@Mixin(Disable.class)
public class TweakerooDisableConfigsMixin {
    @Mutable
    @Final
    @Shadow(remap = false)
    public static ImmutableList<IHotkeyTogglable> OPTIONS;

    @Inject(method = "Lfi/dy/masa/tweakeroo/config/Configs$Disable;<clinit>()V", at = @At("TAIL"))
    private static void staticInit(CallbackInfo callbackInfo) {
        List<IHotkeyTogglable> newOptions = new ArrayList<>();
        newOptions.addAll(TweakerooConfigMixinHelper.getDeclaredHotkeyOptions(Disable.class));
        newOptions.addAll(TweakerooConfigMixinHelper.getDeclaredHotkeyOptions(AdditionalDisableConfig.class));
        OPTIONS = ImmutableList.copyOf(newOptions);
    }
}

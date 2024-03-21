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
import xyz.eclipseisoffline.eclipsestweakeroo.util.EclipsesTweakerooUtil;

@Mixin(Disable.class)
public abstract class TweakerooDisableConfigsMixin {

    @Mutable
    @Final
    @Shadow(remap = false)
    public static ImmutableList<IHotkeyTogglable> OPTIONS;

    @Inject(method = "<clinit>()V", at = @At("TAIL"))
    private static void addAdditionalEntries(CallbackInfo callbackInfo) {
        List<IHotkeyTogglable> newOptions = new ArrayList<>(OPTIONS);
        newOptions.addAll(
                EclipsesTweakerooUtil.getDeclaredHotkeyOptions(AdditionalDisableConfig.class));
        OPTIONS = ImmutableList.copyOf(newOptions);
    }
}

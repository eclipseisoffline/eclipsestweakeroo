package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.tweakeroo.config.Configs.Generic;
import java.util.ArrayList;
import java.util.List;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.TweakerooConfigMixinHelper;

@Mixin(Generic.class)
public class TweakerooGenericConfigsMixin {
    @Mutable
    @Final
    @Shadow(remap = false)
    public static ImmutableList<IConfigBase> OPTIONS;

    @Inject(method = "<clinit>()V", at = @At("TAIL"))
    private static void staticInit(CallbackInfo callbackInfo) {
        List<IConfigBase> newOptions = new ArrayList<>();
        newOptions.addAll(TweakerooConfigMixinHelper.getDeclaredOptions(Generic.class));
        newOptions.addAll(TweakerooConfigMixinHelper.getDeclaredOptions(AdditionalGenericConfig.class));
        OPTIONS = ImmutableList.copyOf(newOptions);
    }
}

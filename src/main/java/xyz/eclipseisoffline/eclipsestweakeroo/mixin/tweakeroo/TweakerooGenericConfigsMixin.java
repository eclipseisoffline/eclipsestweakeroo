package xyz.eclipseisoffline.eclipsestweakeroo.mixin.tweakeroo;

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
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.EclipsesTweakerooUtil;

@Mixin(Generic.class)
public abstract class TweakerooGenericConfigsMixin {

    @Mutable
    @Final
    @Shadow(remap = false)
    public static ImmutableList<IConfigBase> OPTIONS;

    @Inject(method = "<clinit>()V", at = @At("TAIL"))
    private static void addAdditionalEntries(CallbackInfo callbackInfo) {
        List<IConfigBase> newOptions = new ArrayList<>(OPTIONS);
        newOptions.addAll(EclipsesTweakerooUtil.getDeclaredOptions(EclipsesGenericConfig.class));
        OPTIONS = ImmutableList.copyOf(newOptions);
    }
}

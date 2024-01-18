package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.tweakeroo.config.FeatureToggle;
import java.util.ArrayList;
import java.util.List;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.config.TweakerooConfigMixinHelper;

@Mixin(FeatureToggle.class)
public class TweakerooFeatureToggleMixin {
    @Mutable
    @Final
    @Shadow(remap = false)
    public static ImmutableList<FeatureToggle> VALUES;

    @Inject(method = "<clinit>()V", at = @At("TAIL"))
    private static void staticInit(CallbackInfo callbackInfo) {
        List<FeatureToggle> newValues = new ArrayList<>(VALUES);
        newValues.addAll(TweakerooConfigMixinHelper.getDeclaredFeatureToggles(
                AdditionalFeatureToggle.class));
        VALUES = ImmutableList.copyOf(newValues);
    }
}

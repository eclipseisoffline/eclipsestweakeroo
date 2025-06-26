package xyz.eclipseisoffline.eclipsestweakeroo.mixin.tweakeroo;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigBoolean;
import fi.dy.masa.malilib.config.IConfigNotifiable;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
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
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.util.EclipsesTweakerooUtil;

@Mixin(FeatureToggle.class)
public abstract class TweakerooFeatureToggleMixin implements IHotkeyTogglable,
        IConfigNotifiable<IConfigBoolean> {

    @Mutable
    @Final
    @Shadow(remap = false)
    public static ImmutableList<FeatureToggle> VALUES;

    @Inject(method = "<clinit>()V", at = @At("TAIL"))
    private static void addAdditionalEntries(CallbackInfo callbackInfo) {
        List<FeatureToggle> newValues = new ArrayList<>(VALUES);
        newValues.addAll(EclipsesTweakerooUtil.getDeclaredFeatureToggles(
                EclipsesFeatureToggle.class));
        VALUES = ImmutableList.copyOf(newValues);
    }
}

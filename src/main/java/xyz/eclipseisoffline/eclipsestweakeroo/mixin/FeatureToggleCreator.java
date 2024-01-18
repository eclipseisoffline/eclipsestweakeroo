package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import fi.dy.masa.tweakeroo.config.FeatureToggle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FeatureToggle.class)
public interface FeatureToggleCreator {

    @Invoker("<init>")
    static FeatureToggle constructorInvoker(String enumName, int ordinal, String name, boolean defaultValue, boolean singlePlayer, String defaultHotkey, String comment, String prettyName) {
        throw new AssertionError();
    }
}

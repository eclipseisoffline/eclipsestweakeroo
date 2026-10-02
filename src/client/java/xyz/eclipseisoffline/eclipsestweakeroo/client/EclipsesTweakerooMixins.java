package xyz.eclipseisoffline.eclipsestweakeroo.client;

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import xyz.eclipseisoffline.eclipsestweakeroo.client.util.TweakerooFinder;

public class EclipsesTweakerooMixins implements IMixinConfigPlugin {

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return TweakerooFinder.hasTweakeroo() || !mixinClassName.startsWith("xyz.eclipseisoffline.eclipsestweakeroo.client.mixin.tweakeroo");
    }
}

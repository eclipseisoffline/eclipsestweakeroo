package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "shouldRenderName", at = @At("TAIL"), cancellable = true)
    public void shouldRenderName(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        //noinspection ConstantValue
        callbackInfoReturnable.setReturnValue(callbackInfoReturnable.getReturnValue()
                || (AdditionalFeatureToggle.TWEAK_MOB_NAMES.getBooleanValue()
                && ((Object) this instanceof LivingEntity)));
    }
}

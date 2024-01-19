package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalGenericConfig;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "takeKnockback", at = @At("HEAD"), cancellable = true)
    public void takeKnockback(CallbackInfo callbackInfo) {
        //noinspection ConstantValue
        if (((Object) this instanceof PlayerEntity)
                && AdditionalDisableConfig.DISABLE_KNOCKBACK.getBooleanValue()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "shouldRenderName", at = @At("TAIL"), cancellable = true)
    public void shouldRenderName(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        //noinspection ConstantValue
        callbackInfoReturnable.setReturnValue(callbackInfoReturnable.getReturnValue()
                || (AdditionalFeatureToggle.TWEAK_MOB_NAMES.getBooleanValue()
                && ((Object) this instanceof LivingEntity)));
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getSlipperiness()F"))
    public float getSlipperiness(Block block) {
        //noinspection ConstantValue
        if (AdditionalFeatureToggle.TWEAK_SLIPPERY.getBooleanValue()
                && (Object) this instanceof PlayerEntity) {
            return (float) AdditionalGenericConfig.TWEAK_SLIPPERY_SLIPPERINESS.getDoubleValue();
        }

        return block.getSlipperiness();
    }
}

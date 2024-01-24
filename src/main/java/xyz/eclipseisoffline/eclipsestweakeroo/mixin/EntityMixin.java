package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalGenericConfig;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "Lnet/minecraft/entity/Entity;pushAwayFrom(Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
    public void pushAwayFrom(Entity entity, CallbackInfo callbackInfo) {
        //noinspection ConstantValue
        if (((Object) this instanceof PlayerEntity)
                && AdditionalDisableConfig.DISABLE_ENTITY_COLLISIONS.getBooleanValue()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "getJumpVelocityMultiplier", at = @At("HEAD"), cancellable = true)
    public void getJumpVelocityMultiplier(CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (AdditionalFeatureToggle.TWEAK_JUMP_VELOCITY.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(
                    (float) AdditionalGenericConfig.TWEAK_JUMP_VELOCITY.getDoubleValue());
        }
    }
}

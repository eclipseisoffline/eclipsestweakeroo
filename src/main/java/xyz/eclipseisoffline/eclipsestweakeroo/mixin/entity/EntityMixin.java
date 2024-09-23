package xyz.eclipseisoffline.eclipsestweakeroo.mixin.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalGenericConfig;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "push(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
    public void cancelPush(Entity entity, CallbackInfo callbackInfo) {
        //noinspection ConstantValue
        if (((Object) this instanceof Player)
                && AdditionalDisableConfig.DISABLE_ENTITY_COLLISIONS.getBooleanValue()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "getBlockJumpFactor", at = @At("HEAD"), cancellable = true)
    public void getTweakedJumpFactor(CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (AdditionalFeatureToggle.TWEAK_JUMP_VELOCITY.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue((float) AdditionalGenericConfig.TWEAK_JUMP_VELOCITY.getDoubleValue());
        }
    }
}

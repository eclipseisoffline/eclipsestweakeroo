package xyz.eclipseisoffline.eclipsestweakeroo.mixin.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "push(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
    public void cancelPush(Entity entity, CallbackInfo callbackInfo) {
        //noinspection ConstantValue
        if (((Object) this instanceof Player)
                && ToggleManager.enabled(EclipsesDisableConfig.DISABLE_ENTITY_COLLISIONS)) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "getBlockJumpFactor", at = @At("HEAD"), cancellable = true)
    public void getTweakedJumpFactor(CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_JUMP_VELOCITY)) {
            callbackInfoReturnable.setReturnValue((float) EclipsesGenericConfig.TWEAK_JUMP_VELOCITY.getDoubleValue());
        }
    }
}

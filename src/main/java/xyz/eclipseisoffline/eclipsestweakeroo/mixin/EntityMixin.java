package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;

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
}

package xyz.eclipseisoffline.eclipsestweakeroo.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "shouldShowName", at = @At("TAIL"), cancellable = true)
    public void alwaysShowName(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(callbackInfoReturnable.getReturnValue() || (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_MOB_NAMES)));
    }

    @WrapOperation(method = "travelInAir", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getFriction()F"))
    public float getTweakedFriction(Block instance, Operation<Float> original) {
        //noinspection ConstantValue
        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_SLIPPERY)
                && ((Object) this instanceof LocalPlayer
                    || (getControllingPassenger() instanceof LocalPlayer && EclipsesGenericConfig.TWEAK_SLIPPERY_VEHICLES.getBooleanValue()))) {
            return (float) EclipsesGenericConfig.TWEAK_SLIPPERY_SLIPPERINESS.getDoubleValue();
        }

        return original.call(instance);
    }
}

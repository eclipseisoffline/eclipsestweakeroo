package xyz.eclipseisoffline.eclipsestweakeroo.mixin.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.HappyGhast;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;

@Mixin(HappyGhast.class)
public abstract class HappyGhastMixin extends Animal {

    protected HappyGhastMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "getRiddenInput", at = @At("HEAD"), cancellable = true)
    public void useCreativeFlyInput(Player player, Vec3 travelVector, CallbackInfoReturnable<Vec3> callbackInfoReturnable) {
        if (EclipsesTweaksConfig.TWEAK_HAPPY_GHAST.getBooleanValue() && EclipsesGenericConfig.HAPPY_GHAST_CREATIVE_FLIGHT.getBooleanValue()
                && player instanceof LocalPlayer localPlayer) {
            Input input = localPlayer.input.keyPresses;
            double speed = 3.9F * getAttributeValue(Attributes.FLYING_SPEED);

            double x = input.left() && !input.right() ? speed : input.right() ? -speed : 0.0;
            double y = input.jump() && !input.shift() ? speed : input.shift() ? -speed : 0.0;
            double z = input.forward() && !input.backward() ? input.sprint() ? speed * 2.5F : speed : input.backward() ? -speed : 0.0;

            callbackInfoReturnable.setReturnValue(new Vec3(x, y, z));
        }
    }

    @Inject(method = "tickRidden", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/HappyGhast;getRiddenRotation(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/phys/Vec2;"), cancellable = true)
    public void dontRotateIfNotMoving(Player player, Vec3 travelVector, CallbackInfo callbackInfo) {
        if (EclipsesTweaksConfig.TWEAK_HAPPY_GHAST.getBooleanValue() && EclipsesGenericConfig.NO_HAPPY_GHAST_ROTATION.getBooleanValue()
                && player instanceof LocalPlayer && travelVector.lengthSqr() < 0.005) {
            callbackInfo.cancel();
        }
    }

    @ModifyConstant(method = "tickRidden", constant = @Constant(floatValue = 0.08F))
    public float modifyRotationLerpSpeed(float original, @Local(argsOnly = true) Player player) {
        if (EclipsesTweaksConfig.TWEAK_HAPPY_GHAST.getBooleanValue() && player instanceof LocalPlayer) {
            return (float) EclipsesGenericConfig.HAPPY_GHAST_ROTATION_LERP_SPEED.getDoubleValue();
        }
        return original;
    }
}

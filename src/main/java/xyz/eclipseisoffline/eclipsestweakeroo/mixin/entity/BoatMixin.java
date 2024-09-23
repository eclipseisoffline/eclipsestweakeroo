package xyz.eclipseisoffline.eclipsestweakeroo.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalGenericConfig;

@Mixin(Boat.class)
public abstract class BoatMixin extends net.minecraft.world.entity.vehicle.VehicleEntity implements Leashable, VariantHolder<Boat.Type> {

    @Shadow
    private double lerpYRot;
    @Shadow
    private boolean inputLeft;
    @Shadow
    private boolean inputRight;
    @Shadow
    private boolean inputUp;
    @Shadow
    private float deltaRotation;

    public BoatMixin(net.minecraft.world.entity.EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @WrapOperation(method = "getGroundFriction", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getFriction()F"))
    public float getTweakedFriction(Block instance, Operation<Float> original) {
        if (AdditionalFeatureToggle.TWEAK_SLIPPERY.getBooleanValue()
                && getFirstPassenger() instanceof Player
                && AdditionalGenericConfig.TWEAK_SLIPPERY_VEHICLES.getBooleanValue()) {
            return (float) AdditionalGenericConfig.TWEAK_SLIPPERY_SLIPPERINESS.getDoubleValue();
        }

        return original.call(instance);
    }

    @Inject(method = "floatBoat", at = @At("TAIL"))
    public void clearYawVelocityAndJump(CallbackInfo callbackInfo) {
        if (AdditionalFeatureToggle.TWEAK_BOATS.getBooleanValue()) {
            if (!inputLeft && !inputRight) {
                deltaRotation = 0.0F;
            }
            if (inputUp && (onGround() || AdditionalGenericConfig.TWEAK_BOAT_SPIDER.getBooleanValue())) {
                Direction face = getDirection().getOpposite();

                BlockPos inFrontPos = blockPosition().relative(getDirection(), 1);
                BlockPos abovePos = inFrontPos.above();

                BlockState inFrontState = level().getBlockState(inFrontPos);
                VoxelShape inFrontShape = inFrontState.getCollisionShape(level(), inFrontPos).getFaceShape(face);
                double inFrontY = inFrontShape.max(Direction.Axis.Y);

                BlockState aboveState = level().getBlockState(abovePos);
                VoxelShape aboveShape = aboveState.getCollisionShape(level(), abovePos).getFaceShape(face);
                double aboveY = Math.max(0.0, aboveShape.max(Direction.Axis.Y));

                if (inFrontY > 0.0) {
                    double currentY = position().y;
                    double y = 0.0;
                     if (AdditionalGenericConfig.TWEAK_BOAT_SPIDER.getBooleanValue()) {
                         y = 1.0;
                     } else if (inFrontY < 1.0) {
                         y = inFrontY;
                     } else if (inFrontY <= 1.0 && aboveY <= 0.5) {
                         y = inFrontY + aboveY;
                     } else if (inFrontY <= 1.5 && aboveY <= 0.0) {
                         y = inFrontY;
                     }
                     if (y > 0.0 && currentY < inFrontPos.getY() + y) {
                         setPos(position().add(0.0, y + 0.3, 0.0));
                     }
                }
            }
        }
    }

    @Inject(method = "controlBoat", at = @At("HEAD"))
    public void copyPlayerYaw(CallbackInfo callbackInfo) {
        if (isControlledByLocalInstance()
                && getControllingPassenger() != null
                && AdditionalGenericConfig.TWEAK_BOAT_PLAYER_YAW.getBooleanValue()
                && AdditionalFeatureToggle.TWEAK_BOATS.getBooleanValue()) {
            if (Math.abs(getYRot() - getControllingPassenger().getYRot()) > 1) {
                setYRot(getControllingPassenger().getYRot());
                lerpYRot = getControllingPassenger().getYRot();
            }
        }
    }

    @ModifyVariable(method = "setInput", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public boolean notPressingLeftWhenUsingPlayerYaw(boolean inputLeft) {
        if (AdditionalGenericConfig.TWEAK_BOAT_PLAYER_YAW.getBooleanValue()
                && AdditionalFeatureToggle.TWEAK_BOATS.getBooleanValue()) {
            return false;
        }
        return inputLeft;
    }

    @ModifyVariable(method = "setInput", at = @At("HEAD"), ordinal = 1, argsOnly = true)
    public boolean notPressingRightWhenUsingPlayerYaw(boolean inputRight) {
        if (AdditionalGenericConfig.TWEAK_BOAT_PLAYER_YAW.getBooleanValue()
                && AdditionalFeatureToggle.TWEAK_BOATS.getBooleanValue()) {
            return false;
        }
        return inputRight;
    }
}

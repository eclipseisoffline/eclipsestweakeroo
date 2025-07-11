package xyz.eclipseisoffline.eclipsestweakeroo.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractBoat;
import net.minecraft.world.entity.vehicle.VehicleEntity;
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
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.toggle.ServerSideToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

@Mixin(AbstractBoat.class)
public abstract class AbstractBoatMixin extends VehicleEntity implements Leashable {

    @Shadow
    private boolean inputLeft;
    @Shadow
    private boolean inputRight;
    @Shadow
    private boolean inputUp;
    @Shadow
    private float deltaRotation;

    public AbstractBoatMixin(net.minecraft.world.entity.EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @WrapOperation(method = "getGroundFriction", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getFriction()F"))
    public float getTweakedFriction(Block instance, Operation<Float> original) {
        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_SLIPPERY)
                && getFirstPassenger() instanceof Player
                && EclipsesGenericConfig.TWEAK_SLIPPERY_VEHICLES.getBooleanValue()) {
            return (float) EclipsesGenericConfig.TWEAK_SLIPPERY_SLIPPERINESS.getDoubleValue();
        }

        return original.call(instance);
    }

    @Inject(method = "floatBoat", at = @At("TAIL"))
    public void clearYawVelocityAndJump(CallbackInfo callbackInfo) {
        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_BOATS)) {
            if (!inputLeft && !inputRight) {
                deltaRotation = 0.0F;
            }
            boolean allowJumping = ToggleManager.enabled(ServerSideToggle.BOAT_JUMP);
            boolean allowSpiderBoat = EclipsesGenericConfig.TWEAK_BOAT_SPIDER.getBooleanValue() && ToggleManager.enabled(ServerSideToggle.SPIDER_BOAT);
            if (inputUp && allowJumping && (onGround() || allowSpiderBoat)) {
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
                     if (allowSpiderBoat) {
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
        if (isClientAuthoritative()
                && getControllingPassenger() != null
                && EclipsesGenericConfig.TWEAK_BOAT_PLAYER_YAW.getBooleanValue()
                && ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_BOATS)) {
            if (Math.abs(getYRot() - getControllingPassenger().getYRot()) > 1) {
                setYRot(getControllingPassenger().getYRot());
            }
        }
    }

    @ModifyVariable(method = "setInput", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public boolean notPressingLeftWhenUsingPlayerYaw(boolean inputLeft) {
        if (EclipsesGenericConfig.TWEAK_BOAT_PLAYER_YAW.getBooleanValue()
                && ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_BOATS)) {
            return false;
        }
        return inputLeft;
    }

    @ModifyVariable(method = "setInput", at = @At("HEAD"), ordinal = 1, argsOnly = true)
    public boolean notPressingRightWhenUsingPlayerYaw(boolean inputRight) {
        if (EclipsesGenericConfig.TWEAK_BOAT_PLAYER_YAW.getBooleanValue()
                && ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_BOATS)) {
            return false;
        }
        return inputRight;
    }
}

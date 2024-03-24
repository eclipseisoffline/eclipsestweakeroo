package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalGenericConfig;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends Entity {

    @Shadow
    private float yawVelocity;
    @Shadow
    private boolean pressingLeft;
    @Shadow
    private boolean pressingRight;
    @Shadow
    private boolean pressingForward;
    @Shadow
    private double boatYaw;

    protected BoatEntityMixin(EntityType<?> entityType,
            World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract Direction getMovementDirection();

    @Shadow
    @Nullable
    public abstract LivingEntity getControllingPassenger();

    @Redirect(method = "getNearbySlipperiness", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getSlipperiness()F"))
    public float getTweakedSlipperiness(Block block) {
        if (AdditionalFeatureToggle.TWEAK_SLIPPERY.getBooleanValue()
                && getFirstPassenger() instanceof PlayerEntity
                && AdditionalGenericConfig.TWEAK_SLIPPERY_VEHICLES.getBooleanValue()) {
            return (float) AdditionalGenericConfig.TWEAK_SLIPPERY_SLIPPERINESS.getDoubleValue();
        }

        return block.getSlipperiness();
    }

    @Inject(method = "updateVelocity", at = @At("TAIL"))
    public void clearYawVelocityAndJump(CallbackInfo callbackInfo) {
        if (AdditionalFeatureToggle.TWEAK_BOATS.getBooleanValue()) {
            if (!pressingLeft && !pressingRight) {
                yawVelocity = 0;
            }
            if (pressingForward) {
                BlockPos inFrontPos = getBlockPos().offset(getHorizontalFacing(), 1);

                if (AdditionalGenericConfig.TWEAK_BOAT_SPIDER.getBooleanValue()
                        || !getWorld().getBlockState(inFrontPos.up())
                        .isSolidSurface(getWorld(), inFrontPos.up(), this,
                                getHorizontalFacing().getOpposite())) {
                    BlockState block = getWorld().getBlockState(inFrontPos);
                    VoxelShape inFrontShape = block.getCollisionShape(getWorld(), inFrontPos)
                            .offset(inFrontPos.getX(), inFrontPos.getY(), inFrontPos.getZ());

                    double y = inFrontShape.getMax(Axis.Y);
                    if (y > getPos().y && (y - getPos().y) <= 1) {
                        setPosition(getPos().offset(Direction.UP, (y - getPos().y) + 0.3));
                    }
                }
            }
        }
    }

    @Inject(method = "updatePositionAndRotation", at = @At("HEAD"))
    public void copyPlayerYaw(CallbackInfo callbackInfo) {
        if (isLogicalSideForUpdatingMovement()
                && getControllingPassenger() != null
                && AdditionalGenericConfig.TWEAK_BOAT_PLAYER_YAW.getBooleanValue()
                && AdditionalFeatureToggle.TWEAK_BOATS.getBooleanValue()) {
            if (Math.abs(getYaw() - getControllingPassenger().getYaw()) > 1) {
                setYaw(getControllingPassenger().getYaw());
                boatYaw = getControllingPassenger().getYaw();
            }
        }
    }

    @Redirect(method = "updatePaddles", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/vehicle/BoatEntity;pressingLeft:Z"))
    public boolean notPressingLeftWhenUsingPlayerYaw(BoatEntity instance) {
        if (AdditionalGenericConfig.TWEAK_BOAT_PLAYER_YAW.getBooleanValue()
                && AdditionalFeatureToggle.TWEAK_BOATS.getBooleanValue()) {
            return false;
        }
        return pressingLeft;
    }

    @Redirect(method = "updatePaddles", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/vehicle/BoatEntity;pressingRight:Z"))
    public boolean notPressingRightWhenUsingPlayerYaw(BoatEntity instance) {
        if (AdditionalGenericConfig.TWEAK_BOAT_PLAYER_YAW.getBooleanValue()
                && AdditionalFeatureToggle.TWEAK_BOATS.getBooleanValue()) {
            return false;
        }
        return pressingRight;
    }
}

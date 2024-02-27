package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalGenericConfig;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends VehicleEntity {

    @Shadow
    private float yawVelocity;

    @Shadow
    private boolean pressingLeft;

    @Shadow
    private boolean pressingRight;
    @Shadow
    private boolean pressingForward;

    protected BoatEntityMixin(EntityType<?> entityType,
            World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract Direction getMovementDirection();

    @Redirect(method = "getNearbySlipperiness", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getSlipperiness()F"))
    public float getSlipperiness(Block block) {
        if (AdditionalFeatureToggle.TWEAK_SLIPPERY.getBooleanValue()
                && getFirstPassenger() instanceof PlayerEntity
                && AdditionalGenericConfig.TWEAK_SLIPPERY_VEHICLES.getBooleanValue()) {
            return (float) AdditionalGenericConfig.TWEAK_SLIPPERY_SLIPPERINESS.getDoubleValue();
        }

        return block.getSlipperiness();
    }

    @Inject(method = "updateVelocity", at = @At("TAIL"))
    public void clearYawVelocity(CallbackInfo callbackInfo) {
        if (AdditionalFeatureToggle.TWEAK_BOATS.getBooleanValue()) {
            if (!pressingLeft && !pressingRight) {
                yawVelocity = 0;
            }
            if (pressingForward) {
                BlockPos inFrontPos = getBlockPos().offset(getHorizontalFacing(), 1);

                if (!getWorld().getBlockState(inFrontPos.up())
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
}

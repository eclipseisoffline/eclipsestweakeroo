package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalGenericConfig;

@Mixin(LivingEntity.class)
public abstract class LivingEntityClientMixin extends Entity {

    public LivingEntityClientMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "shouldRenderName", at = @At("TAIL"), cancellable = true)
    public void alwaysRenderName(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        //noinspection ConstantValue
        callbackInfoReturnable.setReturnValue(callbackInfoReturnable.getReturnValue()
                || (AdditionalFeatureToggle.TWEAK_MOB_NAMES.getBooleanValue()
                && ((Object) this instanceof LivingEntity)));
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getSlipperiness()F"))
    public float getTweakedSlipperiness(Block block) {
        //noinspection ConstantValue
        if (AdditionalFeatureToggle.TWEAK_SLIPPERY.getBooleanValue()
                && ((Object) this instanceof PlayerEntity
                || (getFirstPassenger() instanceof PlayerEntity
                && AdditionalGenericConfig.TWEAK_SLIPPERY_VEHICLES.getBooleanValue()))) {
            return (float) AdditionalGenericConfig.TWEAK_SLIPPERY_SLIPPERINESS.getDoubleValue();
        }

        return block.getSlipperiness();
    }
}

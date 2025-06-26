package xyz.eclipseisoffline.eclipsestweakeroo.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import fi.dy.masa.tweakeroo.config.FeatureToggle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesGenericConfig;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {

    public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @WrapOperation(method = "aiStep", at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;jumpRidingScale:F", opcode = Opcodes.PUTFIELD),
            slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;jumpRidingScale:F", opcode = Opcodes.PUTFIELD, ordinal = 2),
                    to = @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;jumpRidingScale:F", opcode = Opcodes.PUTFIELD, ordinal = 3)))
    public void setJumpStrengthToMax(LocalPlayer instance, float value, Operation<Void> original) {
        if (EclipsesDisableConfig.DISABLE_HORSE_JUMP_CHARGE.getBooleanValue()) {
            original.call(instance, 1.0F);
        } else {
            original.call(instance, value);
        }
    }

    @ModifyExpressionValue(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/PlayerRideableJumping;getJumpCooldown()I"))
    public int noHorseJumpCooldown(int v) {
        if (EclipsesDisableConfig.DISABLE_HORSE_JUMP_CHARGE.getBooleanValue()) {
            return 0;
        }
        return v;
    }

    @Override
    protected double getDefaultGravity() {
        if (EclipsesFeatureToggle.TWEAK_GRAVITY.getBooleanValue()) {
            return EclipsesGenericConfig.TWEAK_GRAVITY_OVERRIDE.getDoubleValue();
        }
        return super.getDefaultGravity();
    }

    @Override
    public float maxUpStep() {
        if (EclipsesFeatureToggle.TWEAK_STEP_HEIGHT.getBooleanValue()) {
            return (float) EclipsesGenericConfig.TWEAK_STEP_HEIGHT_OVERRIDE.getDoubleValue();
        }
        return super.maxUpStep();
    }

    @Inject(method = "isSuppressingSlidingDownLadder", at = @At("HEAD"), cancellable = true)
    public void checkFakeSneaking(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (FeatureToggle.TWEAK_FAKE_SNEAKING.getBooleanValue() && EclipsesGenericConfig.FAKE_SNEAKING_LADDER.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(true);
        }
    }

    @Inject(method = "isShiftKeyDown", at = @At("HEAD"), cancellable = true)
    public void freecamFix(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (FeatureToggle.TWEAK_FREE_CAMERA.getBooleanValue()
                && FeatureToggle.TWEAK_PERMANENT_SNEAK.getBooleanValue()
                && EclipsesGenericConfig.PERMANENT_SNEAK_FREE_CAMERA.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(true);
        }
    }
}

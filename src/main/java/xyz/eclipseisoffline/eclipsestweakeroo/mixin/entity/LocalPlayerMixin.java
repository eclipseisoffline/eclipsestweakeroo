package xyz.eclipseisoffline.eclipsestweakeroo.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalGenericConfig;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {

    public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @WrapOperation(method = "aiStep", at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;jumpRidingScale:F", opcode = Opcodes.PUTFIELD),
            slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;jumpRidingScale:F", opcode = Opcodes.PUTFIELD, ordinal = 2),
                    to = @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;jumpRidingScale:F", opcode = Opcodes.PUTFIELD, ordinal = 3)))
    public void setJumpStrengthToMax(LocalPlayer instance, float value, Operation<Void> original) {
        if (AdditionalDisableConfig.DISABLE_HORSE_JUMP_CHARGE.getBooleanValue()) {
            original.call(instance, 1.0F);
        } else {
            original.call(instance, value);
        }
    }

    @ModifyExpressionValue(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/PlayerRideableJumping;getJumpCooldown()I"))
    public int noHorseJumpCooldown(int v) {
        if (AdditionalDisableConfig.DISABLE_HORSE_JUMP_CHARGE.getBooleanValue()) {
            return 0;
        }
        return v;
    }

    @Override
    protected double getDefaultGravity() {
        if (AdditionalFeatureToggle.TWEAK_GRAVITY_OVERRIDE.getBooleanValue()) {
            return AdditionalGenericConfig.TWEAK_GRAVITY_OVERRIDE.getDoubleValue();
        }
        return super.getDefaultGravity();
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {

    public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @WrapOperation(method = "aiStep", at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;jumpRidingScale:F", opcode = Opcodes.PUTFIELD),
            slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;jumpRidingScale:F", opcode = Opcodes.PUTFIELD, ordinal = 2),
                    to = @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;jumpRidingScale:F", opcode = Opcodes.PUTFIELD, ordinal = 3)))
    public void setJumpStrengthToMax(LocalPlayer instance, float value, Operation<Void> original) {
        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_HORSE_JUMP_CHARGE)) {
            original.call(instance, 1.0F);
        } else {
            original.call(instance, value);
        }
    }

    @ModifyExpressionValue(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/PlayerRideableJumping;getJumpCooldown()I"))
    public int noHorseJumpCooldown(int v) {
        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_HORSE_JUMP_CHARGE)) {
            return 0;
        }
        return v;
    }

    @Override
    public float maxUpStep() {
        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_STEP_HEIGHT)) {
            return (float) EclipsesGenericConfig.TWEAK_STEP_HEIGHT_OVERRIDE.getDoubleValue();
        }
        return super.maxUpStep();
    }

    @WrapOperation(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"))
    public boolean noUseItemSlowdown(LocalPlayer instance, Operation<Boolean> original) {
        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_USE_ITEM_SLOWDOWN)) {
            return false;
        }
        return original.call(instance);
    }

    @WrapOperation(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getSneakingSpeedBonus(Lnet/minecraft/world/entity/LivingEntity;)F"))
    public float useDefaultAttributeValue(LivingEntity entity, Operation<Float> original) {
        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_SWIFT_SNEAK)) {
            return 0.0F;
        }
        return original.call(entity);
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import fi.dy.masa.tweakeroo.config.FeatureToggle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.animal.HappyGhast;
import net.minecraft.world.entity.player.Input;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {

    @Shadow
    public ClientInput input;

    @Shadow
    @Final
    public ClientPacketListener connection;

    @Shadow private Input lastSentInput;
    @Unique
    private int ghastJumpTime = 0;

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
    protected double getDefaultGravity() {
        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_GRAVITY)) {
            return EclipsesGenericConfig.TWEAK_GRAVITY_OVERRIDE.getDoubleValue();
        }
        return super.getDefaultGravity();
    }

    @Override
    public float maxUpStep() {
        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_STEP_HEIGHT)) {
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

    @WrapOperation(method = "tick", at = @At(value = "NEW", target = "net/minecraft/network/protocol/game/ServerboundPlayerInputPacket"))
    public ServerboundPlayerInputPacket noSneakSendWhenHappyGhastTweak(Input input, Operation<ServerboundPlayerInputPacket> original) {
        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_HAPPY_GHAST)
                && getControlledVehicle() instanceof HappyGhast && input.shift()) {
            input = new Input(
                    input.forward(),
                    input.backward(),
                    input.left(),
                    input.right(),
                    input.jump(),
                    false,
                    input.sprint());
        }
        return original.call(input);
    }

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/Tutorial;onInput(Lnet/minecraft/client/player/ClientInput;)V"))
    public void freecamPermanentSneak(CallbackInfo callbackInfo) {
        if (FeatureToggle.TWEAK_FREE_CAMERA.getBooleanValue()
                && FeatureToggle.TWEAK_PERMANENT_SNEAK.getBooleanValue()
                && EclipsesGenericConfig.PERMANENT_SNEAK_FREE_CAMERA.getBooleanValue()) {
            input.keyPresses = new Input(
                    input.keyPresses.forward(),
                    input.keyPresses.backward(),
                    input.keyPresses.left(),
                    input.keyPresses.right(),
                    input.keyPresses.jump(),
                    true,
                    input.keyPresses.sprint());
        }
    }

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;canStartSprinting()Z"))
    public void dismountHappyGhastWhenDoubleTapJump(CallbackInfo callbackInfo, @Local(ordinal = 0) boolean wasJumping, @Local(ordinal = 3) boolean didAutoJump) {
        if (ghastJumpTime > 0) {
            ghastJumpTime--;
        }

        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_HAPPY_GHAST)
                && getControlledVehicle() instanceof HappyGhast
                && !wasJumping && input.keyPresses.jump() && !didAutoJump) {
            if (ghastJumpTime == 0) {
                ghastJumpTime = 7;
            } else {
                input.keyPresses = new Input(
                        input.keyPresses.forward(),
                        input.keyPresses.backward(),
                        input.keyPresses.left(),
                        input.keyPresses.right(),
                        false,
                        true,
                        input.keyPresses.sprint());
                connection.send(new ServerboundPlayerInputPacket(input.keyPresses));
                lastSentInput = input.keyPresses;
            }
        }
    }

    @WrapOperation(method = "modifyInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"))
    public boolean noUseItemSlowdown(LocalPlayer instance, Operation<Boolean> original) {
        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_USE_ITEM_SLOWDOWN)) {
            return false;
        }
        return original.call(instance);
    }

    @WrapOperation(method = "modifyInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAttributeValue(Lnet/minecraft/core/Holder;)D"))
    public double useDefaultAttributeValue(LocalPlayer instance, Holder<Attribute> holder, Operation<Double> original) {
        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_SWIFT_SNEAK)) {
            return DefaultAttributes.getSupplier(EntityType.PLAYER).getValue(holder);
        }
        return original.call(instance, holder);
    }
}

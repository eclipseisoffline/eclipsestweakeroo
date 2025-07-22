package xyz.eclipseisoffline.eclipsestweakeroo.mixin.tweakeroo.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import fi.dy.masa.tweakeroo.config.FeatureToggle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesGenericConfig;

@Mixin(LocalPlayer.class)
@Pseudo
public abstract class LocalPlayerTweakerooMixin extends AbstractClientPlayer {

    public LocalPlayerTweakerooMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @Inject(method = "isSuppressingSlidingDownLadder", at = @At("HEAD"), cancellable = true)
    public void checkFakeSneaking(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (FeatureToggle.TWEAK_FAKE_SNEAKING.getBooleanValue() && EclipsesGenericConfig.FAKE_SNEAKING_LADDER.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(true);
        }
    }

    @WrapOperation(method = "aiStep", at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;crouching:Z", opcode = Opcodes.PUTFIELD))
    public void freecamPermanentSneak(LocalPlayer instance, boolean value, Operation<Void> original) {
        if (FeatureToggle.TWEAK_FREE_CAMERA.getBooleanValue()
                && FeatureToggle.TWEAK_PERMANENT_SNEAK.getBooleanValue()
                && EclipsesGenericConfig.PERMANENT_SNEAK_FREE_CAMERA.getBooleanValue()) {
            value = true;
        }
        original.call(instance, value);
    }
}

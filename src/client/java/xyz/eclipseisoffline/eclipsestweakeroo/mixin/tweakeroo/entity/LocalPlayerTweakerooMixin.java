package xyz.eclipseisoffline.eclipsestweakeroo.mixin.tweakeroo.entity;

import com.mojang.authlib.GameProfile;
import fi.dy.masa.tweakeroo.config.FeatureToggle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesGenericConfig;

@Mixin(LocalPlayer.class)
@Pseudo
public abstract class LocalPlayerTweakerooMixin extends AbstractClientPlayer {

    @Shadow
    public ClientInput input;

    public LocalPlayerTweakerooMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @Inject(method = "isSuppressingSlidingDownLadder", at = @At("HEAD"), cancellable = true)
    public void checkFakeSneaking(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (FeatureToggle.TWEAK_FAKE_SNEAKING.getBooleanValue() && EclipsesGenericConfig.FAKE_SNEAKING_LADDER.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(true);
        }
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
}

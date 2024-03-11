package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.block.Block;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {

    @Inject(method = "getBlockParticle", at = @At("HEAD"), cancellable = true)
    public void noBlockParticles(CallbackInfoReturnable<Block> callbackInfoReturnable) {
        if (AdditionalFeatureToggle.TWEAK_RENDER_OPERATOR_BLOCKS.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(null);
        }
    }
}

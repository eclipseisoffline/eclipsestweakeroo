package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Shadow private float mountJumpStrength;

    @Redirect(method = "tickMovement", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerEntity;mountJumpStrength:F", opcode = Opcodes.PUTFIELD, ordinal = 2))
    public void setJumpStrengthToMax(ClientPlayerEntity instance, float value) {
        if (AdditionalDisableConfig.DISABLE_HORSE_JUMP_CHARGE.getBooleanValue()) {
            mountJumpStrength = 1.0F;
        } else {
            mountJumpStrength = value;
        }
    }
}

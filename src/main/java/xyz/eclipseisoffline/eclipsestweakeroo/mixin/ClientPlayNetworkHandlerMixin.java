package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onEntityVelocityUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocityClient(DDD)V"), cancellable = true)
    public void onEntityVelocityUpdate(EntityVelocityUpdateS2CPacket packet,
            CallbackInfo callbackInfo) {
        World world = MinecraftClient.getInstance().world;
        assert world != null;

        Entity entity = world.getEntityById(packet.getId());
        if (AdditionalDisableConfig.DISABLE_KNOCKBACK.getBooleanValue()) {
            assert entity != null;
            if (entity.equals(MinecraftClient.getInstance().player)) {
                callbackInfo.cancel();
            }
        }
    }
}

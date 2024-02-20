package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import java.util.function.Consumer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityTrackerEntry.class)
public class EntityTrackerEntryMixin {

    @Shadow @Final private Entity entity;

    @Inject(method = "sendPackets", at = @At("TAIL"))
    public void sendStatusEffectPackets(ServerPlayerEntity player,
            Consumer<Packet<ClientPlayPacketListener>> sender, CallbackInfo callbackInfo) {
        if (entity instanceof LivingEntity livingEntity) {
            for (StatusEffectInstance effect : livingEntity.getStatusEffects()) {
                sender.accept(new EntityStatusEffectS2CPacket(entity.getId(), effect));
            }
        }
    }
}

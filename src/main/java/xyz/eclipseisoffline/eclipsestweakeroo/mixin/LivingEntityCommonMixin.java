package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.RemoveEntityStatusEffectS2CPacket;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityCommonMixin extends Entity {

    protected LivingEntityCommonMixin(EntityType<?> type,
            World world) {
        super(type, world);
    }

    @Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At("RETURN"))
    public void sendStatusEffectToPlayers(StatusEffectInstance effect, Entity source,
            CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (!getWorld().isClient && callbackInfoReturnable.getReturnValue()) {
            assert getWorld().getServer() != null;
            getWorld().getServer().getPlayerManager()
                    .sendToAll(new EntityStatusEffectS2CPacket(getId(), effect));
        }
    }

    @Inject(method = "onStatusEffectRemoved", at = @At("RETURN"))
    public void sendStatusEffectRemovalToPlayers(StatusEffectInstance effect, CallbackInfo ci) {
        if (!getWorld().isClient) {
            assert getWorld().getServer() != null;
            getWorld().getServer().getPlayerManager().sendToAll(
                    new RemoveEntityStatusEffectS2CPacket(getId(), effect.getEffectType()));
        }
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.mixin.entity;

import java.util.List;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    @Final
    private Abilities abilities;

    @Unique
    private boolean creativeFallFlying = false;

    @Shadow
    public abstract void stopFallFlying();

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setPos(DDD)V"))
    public void ignoreWorldBorderBlock(Player instance, double x, double y, double z, Operation<Void> original) {
        if (AdditionalDisableConfig.DISABLE_WORLD_BORDER.getBooleanValue()) {
            return;
        }
        original.call(instance, x, y, z);
    }

    @Inject(method = "startFallFlying", at = @At("TAIL"))
    public void startCreativeFly(CallbackInfo callbackInfo) {
        if (AdditionalFeatureToggle.TWEAK_CREATIVE_ELYTRA_FLIGHT.getBooleanValue()) {
            //noinspection EqualsBetweenInconvertibleTypes
            if (equals(Minecraft.getInstance().player)) {
                abilities.mayfly = true;
                abilities.flying = true;
                creativeFallFlying = true;
            }
        }
    }

    @Override
    public void onSyncedDataUpdated(List<SynchedEntityData.DataValue<?>> newData) {
        if (!creativeFallFlying) {
            return;
        }

        // Stop creative flying when the server no longer thinks we're using an elytra
        for (SynchedEntityData.DataValue<?> data : newData) {
            if (data.id() == Entity.DATA_SHARED_FLAGS_ID.id()) {
                if (!getSharedFlag(Entity.FLAG_FALL_FLYING) && !Minecraft.getInstance().isSingleplayer()) {
                    stopFallFlying();
                }
                break;
            }
        }
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    public void stopFallFlyingWhenNotCreativeFlying(CallbackInfo callbackInfo) {
        if (creativeFallFlying && !abilities.flying) {
            stopFallFlying();
        }
    }

    @Inject(method = "stopFallFlying", at = @At("TAIL"))
    public void stopCreativeFly(CallbackInfo callbackInfo) {
        //noinspection EqualsBetweenInconvertibleTypes
        if (!equals(Minecraft.getInstance().player)) {
            return;
        }

        assert Minecraft.getInstance().getConnection() != null;

        PlayerInfo thisPlayer = Minecraft.getInstance().getConnection().getPlayerInfo(getUUID());
        if (thisPlayer == null) {
            throw new AssertionError();
        }
        GameType gameMode = thisPlayer.getGameMode();
        abilities.mayfly = !gameMode.isSurvival();
        abilities.flying = !gameMode.isSurvival();
        creativeFallFlying = false;
    }
}

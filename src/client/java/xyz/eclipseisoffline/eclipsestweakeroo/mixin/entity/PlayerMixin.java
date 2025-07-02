package xyz.eclipseisoffline.eclipsestweakeroo.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

import java.util.List;

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
    @Unique
    private boolean couldFly = false;

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setPos(DDD)V"))
    public void ignoreWorldBorderBlock(Player instance, double x, double y, double z, Operation<Void> original) {
        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_WORLD_BORDER)) {
            return;
        }
        original.call(instance, x, y, z);
    }

    @Inject(method = "startFallFlying", at = @At("TAIL"))
    public void startCreativeFly(CallbackInfo callbackInfo) {
        //noinspection ConstantValue
        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_CREATIVE_ELYTRA_FLIGHT) && (Object) this instanceof LocalPlayer) {
            couldFly = abilities.mayfly;

            abilities.mayfly = true;
            abilities.flying = true;
            creativeFallFlying = true;
        }
    }

    @Override
    public void onSyncedDataUpdated(@NotNull List<SynchedEntityData.DataValue<?>> newData) {
        if (!creativeFallFlying) {
            return;
        }

        // Stop creative flying when the server no longer thinks we're using an elytra
        for (SynchedEntityData.DataValue<?> data : newData) {
            if (data.id() == Entity.DATA_SHARED_FLAGS_ID.id()) {
                if (!getSharedFlag(Entity.FLAG_FALL_FLYING)) {
                    abilities.mayfly = couldFly;
                    abilities.flying = false;
                    creativeFallFlying = false;
                }
                break;
            }
        }
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    public void stopFallFlyingWhenNotCreativeFlying(CallbackInfo callbackInfo) {
        if (creativeFallFlying) {
            if (abilities.flying) {
                setSharedFlag(FLAG_FALL_FLYING, false);
            } else {
                abilities.mayfly = couldFly;
                creativeFallFlying = false;
                setSharedFlag(FLAG_FALL_FLYING, true);
            }
        }
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker.SerializedEntry;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow
    @Final
    private PlayerAbilities abilities;
    @Unique
    private boolean creativeFallFlying = false;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType,
            World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract void stopFallFlying();

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setPosition(DDD)V"))
    public void ignoreWorldBorderBlock(PlayerEntity instance, double x, double y, double z) {
        if (AdditionalDisableConfig.DISABLE_WORLD_BORDER.getBooleanValue()) {
            return;
        }
        instance.setPosition(x, y, z);
    }

    @Inject(method = "startFallFlying", at = @At("TAIL"))
    public void startCreativeFly(CallbackInfo callbackInfo) {
        if (AdditionalFeatureToggle.TWEAK_CREATIVE_ELYTRA_FLIGHT.getBooleanValue()) {
            assert MinecraftClient.getInstance().player != null;
            //noinspection EqualsBetweenInconvertibleTypes
            if (MinecraftClient.getInstance().player.equals(this)) {
                abilities.allowFlying = true;
                abilities.flying = true;
                creativeFallFlying = true;
            }
        }
    }

    @Override
    public void onDataTrackerUpdate(List<SerializedEntry<?>> dataEntries) {
        if (!creativeFallFlying) {
            return;
        }

        // Stop creative flying when the server no longer thinks we're using an elytra
        for (SerializedEntry<?> entry : dataEntries) {
            if (entry.id() == Entity.FLAGS.id()) {
                if (!getFlag(Entity.FALL_FLYING_FLAG_INDEX) && !MinecraftClient.getInstance()
                        .isInSingleplayer()) {
                    stopFallFlying();
                }
                break;
            }
        }
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    public void stopFallFlyingWhenNotCreativeFlying(CallbackInfo callbackInfo) {
        if (creativeFallFlying && !abilities.flying) {
            stopFallFlying();
        }
    }

    @Inject(method = "stopFallFlying", at = @At("TAIL"))
    public void stopCreativeFly(CallbackInfo callbackInfo) {
        assert MinecraftClient.getInstance().player != null;
        //noinspection EqualsBetweenInconvertibleTypes
        if (!MinecraftClient.getInstance().player.equals(this)) {
            return;
        }

        assert MinecraftClient.getInstance().getNetworkHandler() != null;

        PlayerListEntry thisPlayer = MinecraftClient.getInstance().getNetworkHandler()
                .getPlayerListEntry(getUuid());
        if (thisPlayer == null) {
            throw new AssertionError();
        }
        GameMode gameMode = thisPlayer.getGameMode();
        abilities.allowFlying = !gameMode.isSurvivalLike();
        abilities.flying = !gameMode.isSurvivalLike();
        creativeFallFlying = false;
    }
}

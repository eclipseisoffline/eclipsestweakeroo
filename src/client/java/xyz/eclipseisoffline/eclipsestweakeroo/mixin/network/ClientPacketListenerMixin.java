package xyz.eclipseisoffline.eclipsestweakeroo.mixin.network;

import com.mojang.authlib.GameProfile;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.Connection;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

import java.util.Set;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin extends ClientCommonPacketListenerImpl implements ClientGamePacketListener, TickablePacketListener {

    protected ClientPacketListenerMixin(Minecraft minecraft, Connection connection, CommonListenerCookie commonListenerCookie) {
        super(minecraft, connection, commonListenerCookie);
    }

    @Inject(method = "handleSetEntityMotion", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;lerpMotion(DDD)V"), cancellable = true)
    public void cancelPlayerVelocitySet(ClientboundSetEntityMotionPacket packet,
                                        CallbackInfo callbackInfo) {
        Level level = Minecraft.getInstance().level;
        assert level != null;

        Entity entity = level.getEntity(packet.getId());
        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_KNOCKBACK)) {
            assert entity != null;
            if (entity.equals(Minecraft.getInstance().player)) {
                callbackInfo.cancel();
            }
        }
    }

    @Inject(method = "handleExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"), cancellable = true)
    public void cancelPlayerVelocitySet(ClientboundExplodePacket packet, CallbackInfo callbackInfo) {
        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_KNOCKBACK) && EclipsesGenericConfig.DISABLE_EXPLOSION_KNOCKBACK.getBooleanValue()) {
            callbackInfo.cancel();
        }
    }

    @Redirect(method = "handlePlayerInfoRemove", at = @At(value = "INVOKE", target = "Ljava/util/Set;remove(Ljava/lang/Object;)Z"))
    public boolean showPlayerRemoveNotification(Set<PlayerInfo> instance, Object object) {
        assert object instanceof PlayerInfo;
        PlayerInfo entry = (PlayerInfo) object;

        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_PLAYER_INFO_NOTIFICATIONS)
                && EclipsesGenericConfig.PLAYER_ADD_REMOVE_NOTIFICATION.getBooleanValue()) {
            Minecraft.getInstance().gui.getChat().addMessage(
                    Component.literal("Player info entry " + entry.getProfile().getName() + " was removed").withStyle(ChatFormatting.GOLD));
        }

        return instance.remove(entry);
    }

    @Inject(method = "applyPlayerInfoUpdate", at = @At("HEAD"))
    public void showPlayerInfoNotifications(ClientboundPlayerInfoUpdatePacket.Action action,
                                            ClientboundPlayerInfoUpdatePacket.Entry received, PlayerInfo current, CallbackInfo ci) {
        if (!ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_PLAYER_INFO_NOTIFICATIONS)) {
            return;
        }

        GameProfile gameProfile = received.profile();
        if (gameProfile == null) {
            gameProfile = current.getProfile();
        }

        MutableComponent notification = null;
        switch (action) {
            case ADD_PLAYER -> {
                if (EclipsesGenericConfig.PLAYER_ADD_REMOVE_NOTIFICATION.getBooleanValue()) {
                    notification = Component.literal(
                            "Player info entry " + gameProfile.getName() + " was added");
                }
            }
            case UPDATE_GAME_MODE -> {
                if (EclipsesGenericConfig.PLAYER_GAMEMODE_NOTIFICATION.getBooleanValue()) {
                    notification = Component.literal(gameProfile.getName() + " changed gamemode to " + received.gameMode().getName());
                }
            }
            case UPDATE_LISTED -> {
                if (EclipsesGenericConfig.PLAYER_LISTED_NOTIFICATION.getBooleanValue()) {
                    notification = Component.literal(
                            gameProfile.getName() + " is now " + (received.listed() ? "listed" : "unlisted"));
                }
            }
            case UPDATE_DISPLAY_NAME -> {
                if (EclipsesGenericConfig.PLAYER_DISPLAY_NAME_NOTIFICATION.getBooleanValue()) {
                    notification = Component.literal(gameProfile.getName() + "'s display name is now ")
                            .append(received.displayName() == null ? Component.literal("unset") : received.displayName());
                }
            }
        }

        if (notification != null) {
            Minecraft.getInstance().gui.getChat()
                    .addMessage(notification.withStyle(ChatFormatting.GOLD));
        }
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import com.mojang.authlib.GameProfile;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Action;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Entry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalGenericConfig;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onEntityVelocityUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocityClient(DDD)V"), cancellable = true)
    public void cancelPlayerVelocitySet(EntityVelocityUpdateS2CPacket packet,
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

    @Redirect(method = "onPlayerRemove", at = @At(value = "INVOKE", target = "Ljava/util/Set;remove(Ljava/lang/Object;)Z"))
    public boolean showPlayerRemoveNotification(Set<PlayerListEntry> instance, Object object) {
        assert object instanceof PlayerListEntry;
        PlayerListEntry entry = (PlayerListEntry) object;

        if (AdditionalFeatureToggle.TWEAK_PLAYER_INFO_NOTIFICATIONS.getBooleanValue()
                && AdditionalGenericConfig.PLAYER_ADD_REMOVE_NOTIFICATION.getBooleanValue()) {
            MutableText notification = Text.literal(
                    "Player info entry " + entry.getProfile().getName() + " was removed");
            MinecraftClient.getInstance().inGameHud.getChatHud()
                    .addMessage(notification.formatted(Formatting.GOLD));
        }

        return instance.remove(entry);
    }

    @Inject(method = "handlePlayerListAction", at = @At("HEAD"))
    public void showPlayerInfoNotifications(Action action, Entry receivedEntry,
            PlayerListEntry currentEntry, CallbackInfo callbackInfo) {
        if (!AdditionalFeatureToggle.TWEAK_PLAYER_INFO_NOTIFICATIONS.getBooleanValue()) {
            return;
        }

        GameProfile gameProfile = receivedEntry.profile();
        if (gameProfile == null) {
            return;
        }

        MutableText notification = null;
        switch (action) {
            case ADD_PLAYER -> {
                if (AdditionalGenericConfig.PLAYER_ADD_REMOVE_NOTIFICATION.getBooleanValue()) {
                    notification = Text.literal(
                            "Player info entry " + gameProfile.getName() + " was added");
                }
            }
            case UPDATE_GAME_MODE -> {
                if (AdditionalGenericConfig.PLAYER_GAMEMODE_NOTIFICATION.getBooleanValue()) {
                    notification = Text.literal(gameProfile.getName() + " changed gamemode to "
                            + receivedEntry.gameMode().getName());
                }
            }
            case UPDATE_LISTED -> {
                if (AdditionalGenericConfig.PLAYER_LISTED_NOTIFICATION.getBooleanValue()) {
                    notification = Text.literal(
                            gameProfile.getName() + " is now " + (receivedEntry.listed() ? "listed"
                                    : "unlisted"));
                }
            }
            case UPDATE_DISPLAY_NAME -> {
                if (AdditionalGenericConfig.PLAYER_DISPLAY_NAME_NOTIFICATION.getBooleanValue()) {
                    notification = Text.literal(gameProfile.getName() + "'s display name is now ")
                            .append(receivedEntry.displayName() == null ? Text.literal("unset")
                                    : receivedEntry.displayName());
                }
            }
        }

        if (notification != null) {
            MinecraftClient.getInstance().inGameHud.getChatHud()
                    .addMessage(notification.formatted(Formatting.GOLD));
        }
    }
}

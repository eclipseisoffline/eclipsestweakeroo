package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Action;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Entry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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

    @Inject(method = "handlePlayerListAction", at = @At(value = "INVOKE", target = "Ljava/util/Set;add(Ljava/lang/Object;)Z"))
    public void showEntryJoinMessage(Action action, Entry receivedEntry,
            PlayerListEntry currentEntry, CallbackInfo callbackInfo) {
        if (currentEntry.getDisplayName() == null
                || !AdditionalFeatureToggle.TWEAK_PLAYER_LIST.getBooleanValue()
                || !AdditionalGenericConfig.TWEAK_PLAYER_LIST_CHANGE_MESSAGES.getBooleanValue()) {
            return;
        }

        Text joinMessage = Text.translatable("multiplayer.player.joined", currentEntry.getDisplayName()).formatted(Formatting.YELLOW);
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(joinMessage);
    }

    @Inject(method = "handlePlayerListAction", at = @At(value = "INVOKE", target = "Ljava/util/Set;remove(Ljava/lang/Object;)Z"))
    public void showEntryLeaveMessage(Action action, Entry receivedEntry,
            PlayerListEntry currentEntry, CallbackInfo callbackInfo) {
        if (currentEntry.getDisplayName() == null
                || !AdditionalFeatureToggle.TWEAK_PLAYER_LIST.getBooleanValue()
                || !AdditionalGenericConfig.TWEAK_PLAYER_LIST_CHANGE_MESSAGES.getBooleanValue()) {
            return;
        }

        Text leaveMessage = Text.translatable("multiplayer.player.joined", currentEntry.getDisplayName()).formatted(Formatting.YELLOW);
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(leaveMessage);
    }
}

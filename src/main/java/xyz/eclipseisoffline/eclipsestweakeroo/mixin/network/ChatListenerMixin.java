package xyz.eclipseisoffline.eclipsestweakeroo.mixin.network;

import com.mojang.authlib.GameProfile;
import java.time.Instant;

import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.client.multiplayer.chat.ChatTrustLevel;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.world.scores.PlayerTeam;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesListsConfig;

// TODO this needs to be done over
@Mixin(ChatListener.class)
public abstract class ChatListenerMixin {

    @Shadow
    protected abstract ChatTrustLevel evaluateTrustLevel(PlayerChatMessage chatMessage, Component decoratedServerContent, Instant timestamp);

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "showMessageToPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ChatComponent;addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/GuiMessageTag;)V"),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/FilterMask;isEmpty()Z"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/chat/ChatListener;narrateChatMessage(Lnet/minecraft/network/chat/ChatType$Bound;Lnet/minecraft/network/chat/Component;)V")))
    public void modifyMessage(ChatType.Bound boundChatType, PlayerChatMessage chatMessage, Component decoratedServerContent,
                              GameProfile gameProfile, boolean onlyShowSecureChat, Instant timestamp,
                              CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        // TODO - redo - further customise - combine with cancelMessage method? investigate
        if (EclipsesFeatureToggle.TWEAK_CHAT_MESSAGES.getBooleanValue()) {
            String messageBody = getMessageBody(decoratedServerContent);

            ChatTrustLevel messageTrustStatus = evaluateTrustLevel(chatMessage, decoratedServerContent, timestamp);
            GuiMessageTag messageIndicator = messageTrustStatus.createTag(chatMessage);
            MessageSignature messageSignatureData = chatMessage.signature();

            assert minecraft.getConnection() != null;
            PlayerInfo playerEntry = minecraft.getConnection().getPlayerInfo(gameProfile.getId());
            if (playerEntry != null) {
                MutableComponent newMessage = Component.literal("<");
                newMessage.append(PlayerTeam.formatNameForTeam(playerEntry.getTeam(), Component.literal(gameProfile.getName())));
                newMessage.append(Component.literal("> "));
                newMessage.append(Component.literal(messageBody));

                minecraft.gui.getChat().addMessage(boundChatType.decorate(newMessage), messageSignatureData, messageIndicator);
            }
        }
    }

    @Redirect(method = "showMessageToPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ChatComponent;addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/GuiMessageTag;)V"),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/FilterMask;isEmpty()Z"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/chat/ChatListener;narrateChatMessage(Lnet/minecraft/network/chat/ChatType$Bound;Lnet/minecraft/network/chat/Component;)V")))
    public void cancelMessage(ChatComponent instance, Component chatComponent, MessageSignature headerSignature, GuiMessageTag tag) {
        if (EclipsesFeatureToggle.TWEAK_CHAT_MESSAGES.getBooleanValue()) {
            return;
        }
        instance.addMessage(chatComponent, headerSignature, tag);
    }

    @Unique
    private static String getMessageBody(Component decorated) {
        String full = decorated.getString();
        String messageBody = "";
        for (String separator : EclipsesListsConfig.CHAT_MESSAGE_SEPARATORS.getStrings()) {
            String[] split = full.split(separator);
            if (split.length == 1) {
                continue;
            }

            String potentialMessageBody = split[split.length - 1];
            if (potentialMessageBody.length() > messageBody.length()) {
                messageBody = potentialMessageBody;
            }
        }
        return messageBody;
    }
}

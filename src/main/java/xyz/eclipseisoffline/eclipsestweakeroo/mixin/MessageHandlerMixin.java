package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import com.mojang.authlib.GameProfile;
import java.time.Instant;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.client.network.message.MessageTrustStatus;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.network.message.MessageType.Parameters;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalListsConfig;

@Mixin(MessageHandler.class)
public abstract class MessageHandlerMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    protected abstract MessageTrustStatus getStatus(SignedMessage message, Text decorated,
            Instant receptionTimestamp);

    @Inject(method = "processChatMessageInternal", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V"),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/network/message/FilterMask;isPassThrough()Z"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/message/MessageHandler;narrate(Lnet/minecraft/network/message/MessageType$Parameters;Lnet/minecraft/text/Text;)V")))
    public void modifyMessage(Parameters params, SignedMessage message, Text decorated,
            GameProfile sender, boolean onlyShowSecureChat, Instant receptionTimestamp,
            CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (AdditionalFeatureToggle.TWEAK_CHAT_MESSAGES.getBooleanValue()) {
            String messageBody = getMessageBody(decorated);

            MessageTrustStatus messageTrustStatus = getStatus(message, decorated,
                    receptionTimestamp);
            MessageIndicator messageIndicator = messageTrustStatus.createIndicator(message);
            MessageSignatureData messageSignatureData = message.signature();

            assert client.getNetworkHandler() != null;
            PlayerListEntry playerEntry = client.getNetworkHandler()
                    .getPlayerListEntry(sender.getId());
            if (playerEntry != null) {
                MutableText newMessage = Text.literal("<");
                newMessage.append(Team.decorateName(playerEntry.getScoreboardTeam(),
                        Text.of(sender.getName())));
                newMessage.append(Text.of("> "));
                newMessage.append(Text.of(messageBody));

                client.inGameHud.getChatHud()
                        .addMessage(params.applyChatDecoration(newMessage), messageSignatureData,
                                messageIndicator);
            }
        }
    }

    @Redirect(method = "processChatMessageInternal", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V"),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/network/message/FilterMask;isPassThrough()Z"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/message/MessageHandler;narrate(Lnet/minecraft/network/message/MessageType$Parameters;Lnet/minecraft/text/Text;)V")))
    public void cancelMessage(ChatHud instance, Text message, MessageSignatureData signature,
            MessageIndicator indicator) {
        if (AdditionalFeatureToggle.TWEAK_CHAT_MESSAGES.getBooleanValue()) {
            return;
        }
        instance.addMessage(message, signature, indicator);
    }

    @Unique
    private static String getMessageBody(Text decorated) {
        String full = decorated.getString();
        String messageBody = "";
        for (String separator : AdditionalListsConfig.CHAT_MESSAGE_SEPARATORS.getStrings()) {
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

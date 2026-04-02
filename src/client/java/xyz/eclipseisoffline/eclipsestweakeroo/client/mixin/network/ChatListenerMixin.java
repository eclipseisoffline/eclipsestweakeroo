package xyz.eclipseisoffline.eclipsestweakeroo.client.mixin.network;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.scores.PlayerTeam;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;
import xyz.eclipseisoffline.eclipsestweakeroo.client.config.EclipsesListsConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.client.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.client.util.ToggleManager;

// TODO this needs to be done over
@Mixin(ChatListener.class)
public abstract class ChatListenerMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @WrapOperation(method = "showMessageToPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ChatComponent;addPlayerMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/multiplayer/chat/GuiMessageTag;)V"),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/FilterMask;isEmpty()Z"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/chat/ChatListener;narrateChatMessage(Lnet/minecraft/network/chat/ChatType$Bound;Lnet/minecraft/network/chat/Component;)V")))
    public void modifyMessage(ChatComponent instance, Component message, MessageSignature signature, GuiMessageTag tag, Operation<Void> original,
                              @Local(argsOnly = true) GameProfile sender) {
        // TODO - redo - further customise - combine with cancelMessage method? investigate
        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_CHAT_MESSAGES)) {
            String messageBody = getMessageBody(message);

            MutableComponent newMessage = Component.literal("<");
            PlayerInfo playerInfo = minecraft.getConnection().getPlayerInfo(sender.id());
            newMessage.append(PlayerTeam.formatNameForTeam(playerInfo.getTeam(), Component.literal(sender.name())));
            newMessage.append(Component.literal("> "));
            newMessage.append(Component.literal(messageBody));
            original.call(instance, newMessage, signature, tag);
        } else {
            original.call(instance, message, signature, tag);
        }
    }

    @Unique
    private static String getMessageBody(Component message) {
        String full = message.getString();
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

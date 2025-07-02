package xyz.eclipseisoffline.eclipsestweakeroo.mixin.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.TransferState;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.event.AttemptConnectionCallback;

@Mixin(ConnectScreen.class)
public abstract class ConnectScreenMixin extends Screen {

    protected ConnectScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "startConnecting", at = @At("HEAD"))
    private static void registerNewConnection(Screen parent, Minecraft minecraft, ServerAddress serverAddress,
                                              ServerData serverData, boolean isQuickPlay, TransferState transferState, CallbackInfo callbackInfo) {
        AttemptConnectionCallback.EVENT.invoker().connect(serverAddress, serverData);
    }
}

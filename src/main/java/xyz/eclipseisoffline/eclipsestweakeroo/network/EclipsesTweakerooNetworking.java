package xyz.eclipseisoffline.eclipsestweakeroo.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import xyz.eclipseisoffline.eclipsestweakeroo.EclipsesTweakeroo;
import xyz.eclipseisoffline.eclipsestweakeroo.network.protocol.ClientboundEnabledTogglesPacket;
import xyz.eclipseisoffline.eclipsestweakeroo.toggle.ServerSideToggle;

import java.util.List;

public class EclipsesTweakerooNetworking {

    public static void bootstrap() {
        ServerPlayConnectionEvents.JOIN.register((listener, sender, server) -> {
            if (ServerPlayNetworking.canSend(listener, ClientboundEnabledTogglesPacket.TYPE)) {
                List<ServerSideToggle> enabled;
                if (server.isSingleplayer() || (EclipsesTweakeroo.getConfig().operatorsExempt() && server.getPlayerList().isOp(listener.player.getGameProfile()))) {
                    enabled = ServerSideToggle.ALL;
                } else {
                    enabled = EclipsesTweakeroo.getConfig().enabledToggles();
                }
                sender.sendPacket(new ClientboundEnabledTogglesPacket(enabled));
            }
        });
    }
}

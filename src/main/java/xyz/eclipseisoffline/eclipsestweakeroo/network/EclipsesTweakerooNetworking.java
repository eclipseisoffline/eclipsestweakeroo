package xyz.eclipseisoffline.eclipsestweakeroo.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.minecraft.server.players.NameAndId;
import xyz.eclipseisoffline.eclipsestweakeroo.EclipsesTweakeroo;
import xyz.eclipseisoffline.eclipsestweakeroo.network.protocol.ClientboundEnabledTogglesPacket;
import xyz.eclipseisoffline.eclipsestweakeroo.toggle.ServerSideToggle;

import java.util.List;

public class EclipsesTweakerooNetworking {

    public static void bootstrap() {
        PayloadTypeRegistry.configurationS2C().register(ClientboundEnabledTogglesPacket.TYPE, ClientboundEnabledTogglesPacket.STREAM_CODEC);

        ServerConfigurationConnectionEvents.CONFIGURE.register((listener, server) -> {
            if (ServerConfigurationNetworking.canSend(listener, ClientboundEnabledTogglesPacket.TYPE)) {
                List<ServerSideToggle> enabled;
                if (server.isSingleplayer() || (EclipsesTweakeroo.getConfig().operatorsExempt() && server.getPlayerList().isOp(new NameAndId(listener.getOwner())))) {
                    enabled = ServerSideToggle.ALL;
                } else {
                    enabled = EclipsesTweakeroo.getConfig().enabledToggles();
                }
                ServerConfigurationNetworking.send(listener, new ClientboundEnabledTogglesPacket(enabled));
            }
        });
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import xyz.eclipseisoffline.eclipsestweakeroo.EclipsesTweakeroo;
import xyz.eclipseisoffline.eclipsestweakeroo.toggle.ServerSideToggle;

import java.util.List;

public class EclipsesTweakerooNetworking {

    public static void bootstrap() {
        PayloadTypeRegistry.configurationS2C().register(ClientboundDisabledTogglesPacket.TYPE, ClientboundDisabledTogglesPacket.STREAM_CODEC);

        ServerConfigurationConnectionEvents.CONFIGURE.register((listener, server) -> {
            if (ServerConfigurationNetworking.canSend(listener, ClientboundDisabledTogglesPacket.TYPE)) {
                List<ServerSideToggle> disabled;
                if (server.isSingleplayer()) {
                    disabled = List.of();
                } else {
                    if (EclipsesTweakeroo.getConfig().operatorsExempt() && server.getPlayerList().isOp(listener.getOwner())) {
                        disabled = List.of();
                    } else {
                        disabled = EclipsesTweakeroo.getConfig().disabledToggles();
                    }
                }
                ServerConfigurationNetworking.send(listener, new ClientboundDisabledTogglesPacket(disabled));
            }
        });
    }
}

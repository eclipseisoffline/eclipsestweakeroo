package xyz.eclipseisoffline.eclipsestweakeroo.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import xyz.eclipseisoffline.eclipsestweakeroo.EclipsesTweakeroo;
import xyz.eclipseisoffline.eclipsestweakeroo.network.protocol.ClientboundDisabledTogglesPacket;
import xyz.eclipseisoffline.eclipsestweakeroo.network.protocol.ClientboundEnabledTogglesPacket;
import xyz.eclipseisoffline.eclipsestweakeroo.toggle.ServerSideToggle;

import java.util.List;

public class EclipsesTweakerooNetworking {

    public static void bootstrap() {
        // Legacy, remove after protocol bump
        PayloadTypeRegistry.configurationS2C().register(ClientboundDisabledTogglesPacket.TYPE, ClientboundDisabledTogglesPacket.STREAM_CODEC);
        PayloadTypeRegistry.configurationS2C().register(ClientboundEnabledTogglesPacket.TYPE, ClientboundEnabledTogglesPacket.STREAM_CODEC);

        ServerConfigurationConnectionEvents.CONFIGURE.register((listener, server) -> {
            if (ServerConfigurationNetworking.canSend(listener, ClientboundEnabledTogglesPacket.TYPE)) {
                List<ServerSideToggle> enabled;
                if (server.isSingleplayer() || (EclipsesTweakeroo.getConfig().operatorsExempt() && server.getPlayerList().isOp(listener.getOwner()))) {
                    enabled = ServerSideToggle.ALL;
                } else {
                    enabled = EclipsesTweakeroo.getConfig().enabledToggles();
                }
                ServerConfigurationNetworking.send(listener, new ClientboundEnabledTogglesPacket(enabled));
            } else if (ServerConfigurationNetworking.canSend(listener, ClientboundDisabledTogglesPacket.TYPE)) {
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
                ServerConfigurationNetworking.send(listener, new ClientboundDisabledTogglesPacket(disabled.stream().filter(toggle -> !ServerSideToggle.LEGACY_UNSUPPORTED.contains(toggle)).toList()));
            }
        });
    }
}

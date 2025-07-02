package xyz.eclipseisoffline.eclipsestweakeroo.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;

import java.util.List;

public class EclipsesTweakerooNetworking {

    public static void bootstrap() {
        PayloadTypeRegistry.configurationS2C().register(ClientboundDisabledTogglesPacket.TYPE, ClientboundDisabledTogglesPacket.STREAM_CODEC);

        ServerConfigurationConnectionEvents.CONFIGURE.register((listener, server) -> {
            if (ServerConfigurationNetworking.canSend(listener, ClientboundDisabledTogglesPacket.TYPE)) {
                ServerConfigurationNetworking.send(listener, new ClientboundDisabledTogglesPacket(List.of(ClientboundDisabledTogglesPacket.Toggle.CREATIVE_ELYTRA_FLIGHT)));
            }
        });
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import xyz.eclipseisoffline.eclipsestweakeroo.network.protocol.ClientboundEnabledTogglesPacket;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

import java.util.List;

public class EclipsesTweakerooClientNetworking {

    public static void bootstrap() {
        ClientConfigurationNetworking.registerGlobalReceiver(ClientboundEnabledTogglesPacket.TYPE,
                (packet, context) -> ToggleManager.enableToggles(packet.toggles(), true));

        ClientConfigurationConnectionEvents.INIT.register((listener, client) -> ToggleManager.resetEnabledToggles());
        ClientConfigurationConnectionEvents.COMPLETE.register((listener, client) -> {
            if (!ToggleManager.receivedEnabledToggles()) {
                ToggleManager.enableToggles(List.of(), false);
            }
        });
    }
}

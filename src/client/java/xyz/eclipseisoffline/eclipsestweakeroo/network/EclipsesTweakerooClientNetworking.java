package xyz.eclipseisoffline.eclipsestweakeroo.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import xyz.eclipseisoffline.eclipsestweakeroo.toggle.ServerSideToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

public class EclipsesTweakerooClientNetworking {

    public static void bootstrap() {
        ClientConfigurationNetworking.registerGlobalReceiver(ClientboundDisabledTogglesPacket.TYPE,
                (packet, context) -> ToggleManager.disableToggles(packet.toggles(), true));

        ClientConfigurationConnectionEvents.INIT.register((listener, client) -> {
            ToggleManager.resetDisabledToggles();
        });
        ClientConfigurationConnectionEvents.COMPLETE.register((listener, client) -> {
            if (!ToggleManager.receivedDisabledToggles()) {
                ToggleManager.disableToggles(ServerSideToggle.ALL, false);
            }
        });
    }
}

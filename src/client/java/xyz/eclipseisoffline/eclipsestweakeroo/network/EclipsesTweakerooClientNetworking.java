package xyz.eclipseisoffline.eclipsestweakeroo.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import xyz.eclipseisoffline.eclipsestweakeroo.network.protocol.ClientboundDisabledTogglesPacket;
import xyz.eclipseisoffline.eclipsestweakeroo.network.protocol.ClientboundEnabledTogglesPacket;
import xyz.eclipseisoffline.eclipsestweakeroo.toggle.ServerSideToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

import java.util.ArrayList;
import java.util.List;

public class EclipsesTweakerooClientNetworking {

    public static void bootstrap() {
        ClientConfigurationNetworking.registerGlobalReceiver(ClientboundDisabledTogglesPacket.TYPE,
                (packet, context) -> {
                    List<ServerSideToggle> enabled = new ArrayList<>();
                    for (ServerSideToggle toggle : ServerSideToggle.ALL) {
                        if (!ServerSideToggle.LEGACY_UNSUPPORTED.contains(toggle) && !packet.toggles().contains(toggle)) {
                            enabled.add(toggle);
                        }
                    }
                    ToggleManager.enableToggles(enabled, true);
                });
        ClientConfigurationNetworking.registerGlobalReceiver(ClientboundEnabledTogglesPacket.TYPE,
                (packet, context) -> ToggleManager.enableToggles(packet.toggles(), true));

        ClientConfigurationConnectionEvents.INIT.register((listener, client) -> {
            ToggleManager.resetEnabledToggles();
        });
        ClientConfigurationConnectionEvents.COMPLETE.register((listener, client) -> {
            if (!ToggleManager.receivedEnabledToggles()) {
                ToggleManager.enableToggles(List.of(), false);
            }
        });
    }
}

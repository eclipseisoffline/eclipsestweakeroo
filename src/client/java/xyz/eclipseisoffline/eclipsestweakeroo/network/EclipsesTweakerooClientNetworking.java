package xyz.eclipseisoffline.eclipsestweakeroo.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

public class EclipsesTweakerooClientNetworking {

    public static void bootstrap() {
        ClientConfigurationNetworking.registerGlobalReceiver(ClientboundDisabledTogglesPacket.TYPE,
                (packet, context) -> ToggleManager.disableToggles(packet.toggles()));
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.network;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import xyz.eclipseisoffline.eclipsestweakeroo.network.protocol.ClientboundEnabledTogglesPacket;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

import java.util.List;

public class EclipsesTweakerooClientNetworking {

    public static void bootstrap() {
        ClientPlayNetworking.registerGlobalReceiver(ClientboundEnabledTogglesPacket.TYPE,
                (packet, player, response) -> ToggleManager.enableToggles(packet.toggles(), true));

        ToggleListener listener = new ToggleListener();
        ClientPlayConnectionEvents.INIT.register(listener);
        ClientTickEvents.START_CLIENT_TICK.register(listener);
    }

    private static class ToggleListener implements ClientPlayConnectionEvents.Init, ClientTickEvents.StartTick {
        private int receivedTogglesTimer = -1;

        @Override
        public void onPlayInit(ClientPacketListener clientPacketListener, Minecraft minecraft) {
            ToggleManager.resetEnabledToggles();
            receivedTogglesTimer = 150;
        }

        @Override
        public void onStartTick(Minecraft minecraft) {
            if (receivedTogglesTimer >= 0) {
                if (receivedTogglesTimer == 0 && !ToggleManager.receivedEnabledToggles()) {
                    ToggleManager.enableToggles(List.of(), false);
                }
                receivedTogglesTimer--;
            }
        }
    }
}

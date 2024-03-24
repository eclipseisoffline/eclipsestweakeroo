package xyz.eclipseisoffline.eclipsestweakeroo.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;

public interface AttemptConnectionCallback {

    Event<AttemptConnectionCallback> EVENT = EventFactory.createArrayBacked(
            AttemptConnectionCallback.class, (listeners) -> (address, connectionInfo) -> {
                for (AttemptConnectionCallback listener : listeners) {
                    listener.connect(address, connectionInfo);
                }
            });

    void connect(ServerAddress address, ServerInfo connectionInfo);
}

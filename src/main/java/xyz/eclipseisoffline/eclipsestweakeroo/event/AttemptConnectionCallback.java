package xyz.eclipseisoffline.eclipsestweakeroo.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

public interface AttemptConnectionCallback {

    Event<AttemptConnectionCallback> EVENT = EventFactory.createArrayBacked(
            AttemptConnectionCallback.class, (listeners) -> (address, connectionInfo) -> {
                for (AttemptConnectionCallback listener : listeners) {
                    listener.connect(address, connectionInfo);
                }
            });

    void connect(ServerAddress address, ServerData connectionInfo);
}

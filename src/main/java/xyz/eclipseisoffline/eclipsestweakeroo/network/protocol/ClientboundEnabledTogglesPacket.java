package xyz.eclipseisoffline.eclipsestweakeroo.network.protocol;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import xyz.eclipseisoffline.eclipsestweakeroo.EclipsesTweakeroo;
import xyz.eclipseisoffline.eclipsestweakeroo.toggle.ServerSideToggle;

import java.util.List;

public record ClientboundEnabledTogglesPacket(List<ServerSideToggle> toggles) implements FabricPacket {
    public static final PacketType<ClientboundEnabledTogglesPacket> TYPE = PacketType.create(EclipsesTweakeroo.getModdedLocation("enabled_toggles"), ClientboundEnabledTogglesPacket::new);

    private ClientboundEnabledTogglesPacket(FriendlyByteBuf buffer) {
        this(buffer.readList(ServerSideToggle::read));
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeCollection(toggles, ServerSideToggle::write);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}

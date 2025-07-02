package xyz.eclipseisoffline.eclipsestweakeroo.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
import xyz.eclipseisoffline.eclipsestweakeroo.EclipsesTweakeroo;
import xyz.eclipseisoffline.eclipsestweakeroo.toggle.ServerSideToggle;

import java.util.List;

public record ClientboundDisabledTogglesPacket(List<ServerSideToggle> toggles) implements CustomPacketPayload {

    public static final Type<ClientboundDisabledTogglesPacket> TYPE = new Type<>(EclipsesTweakeroo.getModdedLocation("disabled_toggles"));
    public static final StreamCodec<ByteBuf, ClientboundDisabledTogglesPacket> STREAM_CODEC = ServerSideToggle.STREAM_CODEC
            .apply(ByteBufCodecs.list())
            .map(ClientboundDisabledTogglesPacket::new, ClientboundDisabledTogglesPacket::toggles);

    @Override
    public @NotNull Type<ClientboundDisabledTogglesPacket> type() {
        return TYPE;
    }
}

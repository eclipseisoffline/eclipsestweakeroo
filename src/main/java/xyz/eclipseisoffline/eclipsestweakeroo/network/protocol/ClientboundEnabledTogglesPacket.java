package xyz.eclipseisoffline.eclipsestweakeroo.network.protocol;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
import xyz.eclipseisoffline.eclipsestweakeroo.EclipsesTweakeroo;
import xyz.eclipseisoffline.eclipsestweakeroo.toggle.ServerSideToggle;

import java.util.List;

public record ClientboundEnabledTogglesPacket(List<ServerSideToggle> toggles) implements CustomPacketPayload {

    public static final Type<ClientboundEnabledTogglesPacket> TYPE = new Type<>(EclipsesTweakeroo.getModdedLocation("enabled_toggles"));
    public static final StreamCodec<ByteBuf, ClientboundEnabledTogglesPacket> STREAM_CODEC = ServerSideToggle.STREAM_CODEC
            .apply(ByteBufCodecs.list()).map(ClientboundEnabledTogglesPacket::new, ClientboundEnabledTogglesPacket::toggles);

    @Override
    public @NotNull Type<ClientboundEnabledTogglesPacket> type() {
        return TYPE;
    }
}

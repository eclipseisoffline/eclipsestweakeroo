package xyz.eclipseisoffline.eclipsestweakeroo.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.ByIdMap;
import org.jetbrains.annotations.NotNull;
import xyz.eclipseisoffline.eclipsestweakeroo.EclipsesTweakeroo;

import java.util.List;
import java.util.function.IntFunction;

public record ClientboundDisabledTogglesPacket(List<Toggle> toggles) implements CustomPacketPayload {

    public static final Type<ClientboundDisabledTogglesPacket> TYPE = new Type<>(EclipsesTweakeroo.getModdedLocation("disabled_toggles"));
    public static final StreamCodec<ByteBuf, ClientboundDisabledTogglesPacket> STREAM_CODEC = Toggle.STREAM_CODEC
            .apply(ByteBufCodecs.list())
            .map(ClientboundDisabledTogglesPacket::new, ClientboundDisabledTogglesPacket::toggles);

    @Override
    public @NotNull Type<ClientboundDisabledTogglesPacket> type() {
        return TYPE;
    }

    public enum Toggle {
        SLIPPERY,
        JUMP_VELOCITY,
        BOAT_JUMP,
        SPIDER_BOAT,
        CREATIVE_ELYTRA_FLIGHT,
        GRAVITY,
        STEP_HEIGHT,
        NO_ENTITY_COLLISIONS,
        NO_KNOCKBACK,
        NO_USE_ITEM_SLOWDOWN;

        private static final IntFunction<Toggle> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final StreamCodec<ByteBuf, Toggle> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Toggle::ordinal);
    }
}

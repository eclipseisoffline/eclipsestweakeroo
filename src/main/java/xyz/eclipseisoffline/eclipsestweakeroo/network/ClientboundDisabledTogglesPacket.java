package xyz.eclipseisoffline.eclipsestweakeroo.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.ByIdMap;
import org.jetbrains.annotations.NotNull;
import xyz.eclipseisoffline.eclipsestweakeroo.EclipsesTweakeroo;

import java.util.Arrays;
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
        SLIPPERY("tweakSlippery"),
        JUMP_VELOCITY("tweakJumpVelocity"),
        BOAT_JUMP("tweakBoats (boat jumping functionality)"),
        SPIDER_BOAT("tweakBoats (spiderBoat functionality)"),
        CREATIVE_ELYTRA_FLIGHT("tweakCreativeElytraFlight"),
        GRAVITY("tweakGravity"),
        STEP_HEIGHT("tweakStepHeight"),
        NO_ENTITY_COLLISIONS("disableEntityCollisions"),
        NO_KNOCKBACK("disableKnockback"),
        NO_USE_ITEM_SLOWDOWN("disableUseItemSlowdown");

        public static final List<Toggle> ALL = Arrays.asList(values());

        private static final IntFunction<Toggle> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final StreamCodec<ByteBuf, Toggle> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Toggle::ordinal);

        private final String name;

        Toggle(String name) {
            this.name = name;
        }

        public String displayName() {
            return name;
        }
    }
}

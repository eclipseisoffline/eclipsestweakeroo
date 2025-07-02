package xyz.eclipseisoffline.eclipsestweakeroo.toggle;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;

public enum ServerSideToggle implements StringRepresentable {
    SLIPPERY("tweakSlippery"),
    JUMP_VELOCITY("tweakJumpVelocity"),
    BOAT_JUMP("tweakBoats_jump", "tweakBoats (boat jumping functionality)"),
    SPIDER_BOAT("tweakBoats_spider", "tweakBoats (spiderBoat functionality)"),
    CREATIVE_ELYTRA_FLIGHT("tweakCreativeElytraFlight"),
    GRAVITY("tweakGravity"),
    STEP_HEIGHT("tweakStepHeight"),
    NO_ENTITY_COLLISIONS("disableEntityCollisions"),
    NO_KNOCKBACK("disableKnockback"),
    NO_USE_ITEM_SLOWDOWN("disableUseItemSlowdown");

    public static final List<ServerSideToggle> ALL = Arrays.asList(values());

    private static final IntFunction<ServerSideToggle> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, ServerSideToggle> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ServerSideToggle::ordinal);
    public static final Codec<ServerSideToggle> CODEC = StringRepresentable.fromEnum(ServerSideToggle::values);

    private final String name;
    private final String displayName;

    ServerSideToggle(String name) {
        this(name, name);
    }

    ServerSideToggle(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}

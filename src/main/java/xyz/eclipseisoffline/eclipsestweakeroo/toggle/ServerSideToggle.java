package xyz.eclipseisoffline.eclipsestweakeroo.toggle;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;
import xyz.eclipseisoffline.eclipsestweakeroo.EclipsesTweakeroo;

import java.util.Arrays;
import java.util.List;

public enum ServerSideToggle implements StringRepresentable {
    SLIPPERY("tweak_slippery", "tweakSlippery"),
    JUMP_VELOCITY("tweak_jum_velocity", "tweakJumpVelocity"),
    BOAT_JUMP("tweak_boat_jump", "tweakBoats_jump", "tweakBoats (boat jumping functionality)"),
    SPIDER_BOAT("tweak_boat_spider", "tweakBoats_spider", "tweakBoats (spiderBoat functionality)"),
    CREATIVE_ELYTRA_FLIGHT("tweak_creative_elytra_flight", "tweakCreativeElytraFlight"),
    GRAVITY("tweak_gravity", "tweakGravity"),
    STEP_HEIGHT("tweak_step_height", "tweakStepHeight"),
    NO_ENTITY_COLLISIONS("disable_entity_collisions", "disableEntityCollisions"),
    NO_KNOCKBACK("disable_knockback", "disableKnockback"),
    NO_HORSE_JUMP_CHARGE("disable_horse_jump_charge", "disableHorseJumpCharge"),
    NO_USE_ITEM_SLOWDOWN("disable_use_item_slowdown", "disableUseItemSlowdown"),
    NO_OP("no_op", "no_op"), // TODO move this to first position after dropping legacy
    NO_JUMP_DELAY("disable_jump_delay", "disableJumpDelay");

    public static final List<ServerSideToggle> ALL = Arrays.stream(values()).filter(toggle -> toggle != NO_OP).toList();
    public static final List<ServerSideToggle> LEGACY_UNSUPPORTED = List.of(NO_OP, NO_JUMP_DELAY);

    public static final Codec<ServerSideToggle> CODEC = StringRepresentable.fromEnum(ServerSideToggle::values);

    private final ResourceLocation id;
    private final String name;
    private final String displayName;

    ServerSideToggle(String id, String name) {
        this(id, name, name);
    }

    ServerSideToggle(String id, String name, String displayName) {
        this.id = EclipsesTweakeroo.getModdedLocation(id);
        this.name = name;
        this.displayName = displayName;
    }

    public ResourceLocation id() {
        return id;
    }

    public String displayName() {
        return displayName;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }

    public static ServerSideToggle fromId(ResourceLocation location) {
        for (ServerSideToggle toggle : values()) {
            if (toggle.id.equals(location)) {
                return toggle;
            }
        }
        return NO_OP;
    }

    public static ServerSideToggle read(FriendlyByteBuf buffer) {
        return fromId(buffer.readResourceLocation());
    }

    public static void write(FriendlyByteBuf buffer, ServerSideToggle toggle) {
        buffer.writeResourceLocation(toggle.id);
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.util;

import fi.dy.masa.malilib.config.options.ConfigBoolean;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.network.ClientboundDisabledTogglesPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ToggleManager {

    private static final Map<ConfigBoolean, ClientboundDisabledTogglesPacket.Toggle> TOGGLES = Map.of(
            EclipsesTweaksConfig.TWEAK_SLIPPERY, ClientboundDisabledTogglesPacket.Toggle.SLIPPERY,
            EclipsesTweaksConfig.TWEAK_JUMP_VELOCITY, ClientboundDisabledTogglesPacket.Toggle.JUMP_VELOCITY,
            EclipsesTweaksConfig.TWEAK_CREATIVE_ELYTRA_FLIGHT, ClientboundDisabledTogglesPacket.Toggle.CREATIVE_ELYTRA_FLIGHT,
            EclipsesTweaksConfig.TWEAK_GRAVITY, ClientboundDisabledTogglesPacket.Toggle.GRAVITY,
            EclipsesTweaksConfig.TWEAK_STEP_HEIGHT, ClientboundDisabledTogglesPacket.Toggle.STEP_HEIGHT,
            EclipsesDisableConfig.DISABLE_ENTITY_COLLISIONS, ClientboundDisabledTogglesPacket.Toggle.NO_ENTITY_COLLISIONS,
            EclipsesDisableConfig.DISABLE_KNOCKBACK, ClientboundDisabledTogglesPacket.Toggle.NO_KNOCKBACK,
            EclipsesDisableConfig.DISABLE_USE_ITEM_SLOWDOWN, ClientboundDisabledTogglesPacket.Toggle.NO_USE_ITEM_SLOWDOWN
    );

    private static final List<ClientboundDisabledTogglesPacket.Toggle> disabledToggles = new ArrayList<>();

    public static boolean isToggleEnabled(ConfigBoolean toggle) {
        ClientboundDisabledTogglesPacket.Toggle serverControlled = TOGGLES.get(toggle);
        if (serverControlled != null && disabledToggles.contains(serverControlled)) {
            return false;
        }
        return toggle.getBooleanValue();
    }

    public static boolean isDisabled(ClientboundDisabledTogglesPacket.Toggle toggle) {
        return disabledToggles.contains(toggle);
    }

    public static void disableToggles(List<ClientboundDisabledTogglesPacket.Toggle> toggles) {
        disabledToggles.clear();
        disabledToggles.addAll(toggles);
        // TODO broadcast
    }
}

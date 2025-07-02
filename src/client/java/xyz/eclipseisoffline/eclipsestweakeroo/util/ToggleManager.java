package xyz.eclipseisoffline.eclipsestweakeroo.util;

import fi.dy.masa.malilib.config.options.ConfigBoolean;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import xyz.eclipseisoffline.eclipsestweakeroo.EclipsesTweakeroo;
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
    private static boolean receivedDisabledToggles = false;

    public static boolean enabled(ConfigBoolean toggle) {
        ClientboundDisabledTogglesPacket.Toggle serverControlled = TOGGLES.get(toggle);
        if (serverControlled != null && disabledToggles.contains(serverControlled)) {
            return false;
        }
        return toggle.getBooleanValue();
    }

    public static boolean disabled(ClientboundDisabledTogglesPacket.Toggle toggle) {
        return disabledToggles.contains(toggle);
    }

    public static void disableToggles(List<ClientboundDisabledTogglesPacket.Toggle> toggles, boolean installedOnServer) {
        disabledToggles.clear();
        disabledToggles.addAll(toggles);

        receivedDisabledToggles = true;

        if (!disabledToggles.isEmpty()) {
            String reason = installedOnServer ? "by the server" : "because they require a server-side opt-in";

            MutableComponent disableMessage = Component.literal("The following features of " + EclipsesTweakeroo.MOD_NAME_SHORT + " have been disabled " + reason + ": ");
            for (int i = 0; i < disabledToggles.size(); i++) {
                disableMessage.append(Component.literal(disabledToggles.get(i).displayName()).withStyle(ChatFormatting.RED));
                if (i < disabledToggles.size() - 1) {
                    disableMessage.append(Component.literal(", "));
                }
            }
            disableMessage = disableMessage.withStyle(ChatFormatting.GREEN);
            Minecraft.getInstance().getChatListener().handleSystemMessage(disableMessage, false);

            if (!installedOnServer) {
                Minecraft.getInstance().getChatListener().handleSystemMessage(
                        Component.literal("Ask the server administrator to install the " + EclipsesTweakeroo.MOD_NAME_SHORT + " mod on the server to allow using these features")
                                .withStyle(ChatFormatting.GREEN), false);
            }

            Component.literal("Toggle ");
        }
    }

    public static void resetDisabledToggles() {
        disabledToggles.clear();
        receivedDisabledToggles = false;
    }

    public static boolean receivedDisabledToggles() {
        return receivedDisabledToggles;
    }
}

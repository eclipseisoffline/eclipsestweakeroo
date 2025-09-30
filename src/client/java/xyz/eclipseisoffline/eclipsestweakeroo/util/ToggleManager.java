package xyz.eclipseisoffline.eclipsestweakeroo.util;

import fi.dy.masa.malilib.config.options.ConfigBoolean;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import xyz.eclipseisoffline.eclipsestweakeroo.EclipsesTweakeroo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.toggle.ServerSideToggle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ToggleManager {

    private static final Map<ConfigBoolean, ServerSideToggle> TOGGLES = Map.of(
            EclipsesTweaksConfig.TWEAK_SLIPPERY, ServerSideToggle.SLIPPERY,
            EclipsesTweaksConfig.TWEAK_JUMP_VELOCITY, ServerSideToggle.JUMP_VELOCITY,
            EclipsesTweaksConfig.TWEAK_CREATIVE_ELYTRA_FLIGHT, ServerSideToggle.CREATIVE_ELYTRA_FLIGHT,
            EclipsesTweaksConfig.TWEAK_GRAVITY, ServerSideToggle.GRAVITY,
            EclipsesTweaksConfig.TWEAK_STEP_HEIGHT, ServerSideToggle.STEP_HEIGHT,
            EclipsesDisableConfig.DISABLE_ENTITY_COLLISIONS, ServerSideToggle.NO_ENTITY_COLLISIONS,
            EclipsesDisableConfig.DISABLE_KNOCKBACK, ServerSideToggle.NO_KNOCKBACK,
            EclipsesDisableConfig.DISABLE_HORSE_JUMP_CHARGE, ServerSideToggle.NO_HORSE_JUMP_CHARGE,
            EclipsesDisableConfig.DISABLE_USE_ITEM_SLOWDOWN, ServerSideToggle.NO_USE_ITEM_SLOWDOWN,
            EclipsesDisableConfig.DISABLE_JUMP_DELAY, ServerSideToggle.NO_JUMP_DELAY
    );

    private static final List<ServerSideToggle> enabledToggles = new ArrayList<>();
    private static final List<ServerSideToggle> disabledToggles = new ArrayList<>();
    private static boolean receivedEnabledToggles = false;

    public static boolean enabled(ConfigBoolean toggle) {
        ServerSideToggle serverControlled = TOGGLES.get(toggle);
        if (serverControlled != null && !enabledToggles.contains(serverControlled)) {
            return false;
        }
        return toggle.getBooleanValue();
    }

    public static boolean enabled(ServerSideToggle toggle) {
        return enabledToggles.contains(toggle);
    }

    public static void enableToggles(List<ServerSideToggle> toggles, boolean installedOnServer) {
        enabledToggles.clear();
        disabledToggles.clear();
        enabledToggles.addAll(toggles);
        receivedEnabledToggles = true;

        for (ServerSideToggle toggle : ServerSideToggle.ALL) {
            if (!enabledToggles.contains(toggle)) {
                disabledToggles.add(toggle);
            }
        }

        if (EclipsesGenericConfig.SERVER_SIDE_DISABLED_MESSAGE.getBooleanValue() && !disabledToggles.isEmpty()) {
            String reason = installedOnServer ? "by the server" : "because they require a server-side opt-in";

            MutableComponent disableMessageBuilder = Component.literal("The following features of " + EclipsesTweakeroo.MOD_NAME_SHORT + " have been disabled " + reason + ": ");
            for (int i = 0; i < disabledToggles.size(); i++) {
                disableMessageBuilder.append(Component.literal(disabledToggles.get(i).displayName()).withStyle(ChatFormatting.RED));
                if (i < disabledToggles.size() - 1) {
                    disableMessageBuilder.append(Component.literal(", "));
                }
            }
            disableMessageBuilder = disableMessageBuilder.withStyle(ChatFormatting.GREEN);
            Component disableMessage = disableMessageBuilder;
            Minecraft.getInstance().execute(() -> {
                Minecraft.getInstance().getChatListener().handleSystemMessage(disableMessage, false);

                if (!installedOnServer) {
                    Minecraft.getInstance().getChatListener().handleSystemMessage(
                            Component.literal("Ask the server administrator to install the " + EclipsesTweakeroo.MOD_NAME_SHORT + " mod on the server to allow using these features")
                                    .withStyle(ChatFormatting.GREEN), false);
                }

                Minecraft.getInstance().getChatListener().handleSystemMessage(
                        Component.literal("Toggle this message using the ")
                                .append(Component.literal("serverDisabledMessage").withStyle(ChatFormatting.BLUE))
                                .append(" option in the mod's config menu")
                                .withStyle(ChatFormatting.GREEN), false);
            });
        }
    }

    public static void resetEnabledToggles() {
        enabledToggles.clear();
        disabledToggles.clear();
        receivedEnabledToggles = false;
    }

    public static boolean receivedEnabledToggles() {
        return receivedEnabledToggles;
    }
}

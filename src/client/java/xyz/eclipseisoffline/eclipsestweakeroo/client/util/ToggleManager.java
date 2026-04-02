package xyz.eclipseisoffline.eclipsestweakeroo.client.util;

import fi.dy.masa.malilib.config.options.ConfigBoolean;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import xyz.eclipseisoffline.eclipsestweakeroo.EclipsesTweakeroo;
import xyz.eclipseisoffline.eclipsestweakeroo.client.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.client.config.EclipsesGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.client.config.EclipsesTweaksConfig;
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
    private static boolean installedOnServer = false;

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
        ToggleManager.installedOnServer = installedOnServer;

        for (ServerSideToggle toggle : ServerSideToggle.ALL) {
            if (!enabledToggles.contains(toggle)) {
                disabledToggles.add(toggle);
            }
        }
    }

    public static void resetEnabledToggles() {
        enabledToggles.clear();
        disabledToggles.clear();
        receivedEnabledToggles = false;
        ToggleManager.installedOnServer = false;
    }

    public static boolean receivedEnabledToggles() {
        return receivedEnabledToggles;
    }

    public static void bootstrap() {
        ClientPlayConnectionEvents.JOIN.register((_, _, _) -> {
            if (EclipsesGenericConfig.SERVER_SIDE_DISABLED_MESSAGE.getBooleanValue() && !disabledToggles.isEmpty()) {
                Minecraft.getInstance().execute(() -> {
                    System.out.println("sending disabled messages");
                    MutableComponent disabledFeaturesBuilder = Component.empty();
                    for (int i = 0; i < disabledToggles.size(); i++) {
                        disabledFeaturesBuilder.append(Component.literal(disabledToggles.get(i).displayName()).withStyle(ChatFormatting.RED));
                        if (i < disabledToggles.size() - 1) {
                            disabledFeaturesBuilder.append(Component.literal(", "));
                        }
                    }

                    Component disabledMessage;
                    if (installedOnServer) {
                        disabledMessage = Component.translatable(EclipsesTweakerooUtil.getMessageDescriptionId("features_disabled_by_server"), EclipsesTweakeroo.MOD_NAME_SHORT, disabledFeaturesBuilder)
                                .withStyle(ChatFormatting.GREEN);
                    } else {
                        disabledMessage = Component.translatable(EclipsesTweakerooUtil.getMessageDescriptionId("features_disabled_no_opt_in"), EclipsesTweakeroo.MOD_NAME_SHORT, disabledFeaturesBuilder)
                                .withStyle(ChatFormatting.GREEN);
                    }

                    Minecraft.getInstance().getChatListener().handleSystemMessage(disabledMessage, false);

                    if (!installedOnServer) {
                        Minecraft.getInstance().getChatListener().handleSystemMessage(
                                Component.translatable(EclipsesTweakerooUtil.getMessageDescriptionId("request_administrator_for_features"), EclipsesTweakeroo.MOD_NAME_SHORT)
                                        .withStyle(ChatFormatting.GREEN), false);
                    }

                    Minecraft.getInstance().getChatListener().handleSystemMessage(
                            Component.translatable(EclipsesTweakerooUtil.getMessageDescriptionId("enabled_features_toggle"), Component.literal("serverDisabledMessage")
                                            .withStyle(ChatFormatting.BLUE))
                                    .withStyle(ChatFormatting.GREEN), false);
                });
            }
        });
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.config;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;

import java.util.ArrayList;
import java.util.List;

public class EclipsesTweaksConfig {

    private static final List<IConfigBase> CONFIGS = new ArrayList<>();
    
    public static final ConfigBooleanHotkeyed TWEAK_PLAYER_LIST = create("tweakPlayerList");
    public static final ConfigBooleanHotkeyed TWEAK_PLAYER_NAME = create("tweakPlayerNames");
    public static final ConfigBooleanHotkeyed TWEAK_MOB_NAMES = create("tweakMobNames");
    public static final ConfigBooleanHotkeyed TWEAK_CHAT_MESSAGES = create("tweakChatMessages");
    public static final ConfigBooleanHotkeyed TWEAK_SLIPPERY = createWithServerSideOptIn("tweakSlippery");
    public static final ConfigBooleanHotkeyed TWEAK_JUMP_VELOCITY = createWithServerSideOptIn("tweakJumpVelocity");
    public static final ConfigBooleanHotkeyed TWEAK_DURABILITY_CHECK = create("tweakDurabilityCheck");
    public static final ConfigBooleanHotkeyed TWEAK_STATUS_EFFECT = create("tweakStatusEffectHud");
    public static final ConfigBooleanHotkeyed TWEAK_AUTO_RECONNECT = create("tweakAutoReconnect");
    public static final ConfigBooleanHotkeyed TWEAK_PLAYER_INFO_NOTIFICATIONS = create("tweakPlayerInfoNotifications");
    public static final ConfigBooleanHotkeyed TWEAK_BOATS = createWithServerSideOptIn("tweakBoats");
    public static final ConfigBooleanHotkeyed TWEAK_CREATIVE_ELYTRA_FLIGHT = createWithServerSideOptIn("tweakCreativeElytraFlight");
    public static final ConfigBooleanHotkeyed TWEAK_RENDER_OPERATOR_BLOCKS = create("tweakRenderOperatorBlocks");
    public static final ConfigBooleanHotkeyed TWEAK_NUMBER_HUD = create("tweakNumberHud");
    public static final ConfigBooleanHotkeyed TWEAK_LODESTONE = create("tweakLodestoneCompass");
    public static final ConfigBooleanHotkeyed TWEAK_GRAVITY = createWithServerSideOptIn("tweakGravity");
    public static final ConfigBooleanHotkeyed TWEAK_STEP_HEIGHT = createWithServerSideOptIn("tweakStepHeight");
    public static final ConfigBooleanHotkeyed TWEAK_HAPPY_GHAST = create("tweakHappyGhast");
    public static final ConfigBooleanHotkeyed TWEAK_LOCATOR_BAR = create("tweakLocatorBar");
    public static final ConfigBooleanHotkeyed TWEAK_PERSISTENT_CHAT = create("tweakPersistentChat");
    public static final ConfigBooleanHotkeyed TWEAK_SHOW_FORMATTING_CODES = create("tweakShowFormattingCodes");
    public static final ConfigBooleanHotkeyed TWEAK_MUSIC_TOAST = create("tweakMusicToast");

    private static ConfigBooleanHotkeyed create(String name) {
        ConfigBooleanHotkeyed config = new ConfigBooleanHotkeyed(name, false, "", EclipsesConfigs.getCommentTranslationKey(name));
        CONFIGS.add(config);
        return config;
    }

    private static ConfigBooleanHotkeyed createWithServerSideOptIn(String name) {
        ServerSideOptInConfigOption config = new ServerSideOptInConfigOption(name, false, "", EclipsesConfigs.getCommentTranslationKey(name));
        CONFIGS.add(config);
        return config;
    }

    public static List<IConfigBase> values() {
        return List.copyOf(CONFIGS);
    }

    public static List<IHotkeyTogglable> hotkeys() {
        return CONFIGS.stream().map(config -> (IHotkeyTogglable) config).toList();
    }

    public static void bootstrap(Minecraft minecraft) {
        TWEAK_CREATIVE_ELYTRA_FLIGHT.setValueChangeCallback(value -> {
            if (minecraft.player == null) {
                return;
            }
            if (value.getBooleanValue()) {
                if (minecraft.player.isFallFlying()) {
                    minecraft.player.startFallFlying();
                }
            } else {
                minecraft.player.getAbilities().flying = false;
            }
        });

        TWEAK_RENDER_OPERATOR_BLOCKS.setValueChangeCallback(_ -> {
            LevelRenderer levelRenderer = minecraft.levelRenderer;
            levelRenderer.allChanged();
        });
    }
}

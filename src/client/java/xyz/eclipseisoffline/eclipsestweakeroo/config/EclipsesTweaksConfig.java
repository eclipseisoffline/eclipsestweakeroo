package xyz.eclipseisoffline.eclipsestweakeroo.config;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;

import java.util.ArrayList;
import java.util.List;

public class EclipsesTweaksConfig {

    private static final List<IConfigBase> CONFIGS = new ArrayList<>();
    
    public static final ConfigBooleanHotkeyed TWEAK_PLAYER_LIST = create("tweakPlayerList", "Makes various changes to the player list.\nCan be configured in General", "Player list tweak");
    public static final ConfigBooleanHotkeyed TWEAK_PLAYER_NAME = create("tweakPlayerNames", "Uses fancy names for rending the player names", "Player names tweak");
    public static final ConfigBooleanHotkeyed TWEAK_MOB_NAMES = create("tweakMobNames", "Uses fancy names for rendering mob names, and makes them always show", "Mob names tweak");
    public static final ConfigBooleanHotkeyed TWEAK_CHAT_MESSAGES = create("tweakChatMessages", "Tries to disable all server chat formatting, to make chat messages display vanilla-like", "Chat messages tweak");
    public static final ConfigBooleanHotkeyed TWEAK_SLIPPERY = create("tweakSlippery", "Overrides every block's slipperiness. Can be configured in General. Mostly for fun", "Slippery tweak");
    public static final ConfigBooleanHotkeyed TWEAK_JUMP_VELOCITY = create("tweakJumpVelocity", "Overrides every block's jump velocity. Can be configured in General. Also mostly for fun", "Jump velocity tweak");
    public static final ConfigBooleanHotkeyed TWEAK_DURABILITY_CHECK = create("tweakDurabilityCheck",
            "Adds a durability check, which displays a message when\nyour armor or the item you're currently using is low on durability.\nCan also disable using items low on durability by configuring in General",
            "Durability check tweak");
    public static final ConfigBooleanHotkeyed TWEAK_STATUS_EFFECT = create("tweakStatusEffectHud", "Modifies the status effect HUD to show effect duration.", "Status effect HUD tweak");
    public static final ConfigBooleanHotkeyed TWEAK_AUTO_RECONNECT = create("tweakAutoReconnect", "Automatically reconnects on server disconnect", "Auto reconnect tweak");
    public static final ConfigBooleanHotkeyed TWEAK_PLAYER_INFO_NOTIFICATIONS = create("tweakPlayerInfoNotifications", "Puts notifications in chat for player info changes.\nCan be configured in General",
            "Player info notifications tweak");
    public static final ConfigBooleanHotkeyed TWEAK_BOATS = create("tweakBoats", "Disables boat drift on turning and allows boats to jump 1 block high\nConfigure further in General", "Boat tweak");
    public static final ConfigBooleanHotkeyed TWEAK_CREATIVE_ELYTRA_FLIGHT = create("tweakCreativeElytraFlight",
            "Enables creative flight when using elytra.\nMay be considered cheating on some servers, use at your own risk",
            "Creative elytra flight tweak");
    public static final ConfigBooleanHotkeyed TWEAK_RENDER_OPERATOR_BLOCKS = create("tweakRenderOperatorBlocks", "Makes operator blocks like barrier and light blocks have a texture", "Operator blocks render tweak");
    public static final ConfigBooleanHotkeyed TWEAK_NUMBER_HUD = create("tweakNumberHud", "Replaces part of the HUD with number/text-based rendering to be more informative", "Number HUD tweak");
    public static final ConfigBooleanHotkeyed TWEAK_LODESTONE = create("tweakLodestoneCompass", "Enables you to see the position a lodestone compass has targeted by right clicking it", "Lodestone compass tweak");
    public static final ConfigBooleanHotkeyed TWEAK_GRAVITY = create("tweakGravity", "Allows you to override your gravity attribute. Can be configured in General", "Gravity tweak");
    public static final ConfigBooleanHotkeyed TWEAK_STEP_HEIGHT = create("tweakStepHeight", "Allows you to override your step height attribute. Can be configured in General.\nNote that this also affects sneaking",
            "Step height tweak");
    public static final ConfigBooleanHotkeyed TWEAK_HAPPY_GHAST = create("tweakHappyGhast",
            "Makes various tweaks to controlling Happy Ghasts.\nRequires configuring in General",
            "Happy Ghast flight tweak");
    public static final ConfigBooleanHotkeyed TWEAK_LOCATOR_BAR = create("tweakLocatorBar", "Shows faces of players on the locator bar when applicable", "Locator bar tweak");
    public static final ConfigBooleanHotkeyed TWEAK_PERSISTENT_CHAT = create("tweakPersistentChat", "Keep past chat messages across server/world switches", "Persistent chat tweak");
    public static final ConfigBooleanHotkeyed TWEAK_SHOW_FORMATTING_CODES = create("tweakShowFormattingCodes", "Renders legacy text formatting codes instead of hiding them", "Show formatting codes tweak");

    private static ConfigBooleanHotkeyed create(String name, String comment, String prettyName) {
        ConfigBooleanHotkeyed config = new ConfigBooleanHotkeyed(name, false, "", comment, prettyName);
        CONFIGS.add(config);
        return config;
    }

    public static List<IConfigBase> values() {
        return List.copyOf(CONFIGS);
    }

    public static List<IHotkeyTogglable> hotkeys() {
        return CONFIGS.stream().map(config -> (IHotkeyTogglable) config).toList();
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fi.dy.masa.tweakeroo.config.FeatureToggle;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.block.BedBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFixesConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.mixin.DisconnectedScreenAccessor;
import xyz.eclipseisoffline.eclipsestweakeroo.util.EclipsesTweakerooUtil;

public class EclipsesTweakeroo implements ClientModInitializer {
    public static final String MOD_ID = "eclipsestweakeroo";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Map<StatusEffect, String> STATUS_EFFECT_CHARACTER_MAP = new HashMap<>();

    private static final double DURABILITY_WARNING = 0.9;
    private static final Text TO_MENU_TEXT = Text.translatable("gui.toMenu");
    private static ServerAddress lastConnection = null;
    private static ServerInfo lastConnectionInfo = null;
    private static final String FANCYNAME_EFFECT_MAP_PATH = "fancyname";
    private static final String FANCYNAME_EFFECT_MAP_NAME = "effect_map.json";

    private final Map<EquipmentSlot, Item> registeredItems = new HashMap<>();
    private final Map<EquipmentSlot, Integer> registeredWarningTimes = new HashMap<>();

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
            if (AdditionalFixesConfig.GAMMA_OVERRIDE_FIX.getBooleanValue()) {
                FeatureToggle.TWEAK_GAMMA_OVERRIDE.onValueChanged();
            }
        }));

        ClientPreAttackCallback.EVENT.register(((client, player, clickCount) -> !useCheck(player, Hand.MAIN_HAND)));
        UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> {
            if(!useCheck(player, hand)) {
                return ActionResult.FAIL;
            }

            if (AdditionalDisableConfig.DISABLE_BED_EXPLOSION.getBooleanValue()
                    && !world.getDimension().bedWorks()
                    && world.getBlockState(hitResult.getBlockPos()).getBlock() instanceof BedBlock) {
                return ActionResult.FAIL;
            }

            return ActionResult.PASS;
        }));
        UseEntityCallback.EVENT.register(((player, world, hand, entity, hitResult) -> useCheck(player, hand)
                ? ActionResult.PASS : ActionResult.FAIL));
        UseItemCallback.EVENT.register(((player, world, hand) -> useCheck(player, hand)
                ? TypedActionResult.pass(player.getStackInHand(hand)) : TypedActionResult.fail(player.getStackInHand(hand))));

        ClientTickEvents.START_WORLD_TICK.register((world -> {
            assert MinecraftClient.getInstance().player != null;
            int time = EclipsesTweakerooUtil.milliTime();
            for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                ItemStack itemStack = MinecraftClient.getInstance().player.getEquippedStack(equipmentSlot);

                int requiredDamage = (int) (DURABILITY_WARNING * itemStack.getMaxDamage());
                if (!itemStack.isDamageable() || itemStack.getDamage() < requiredDamage) {
                    registeredWarningTimes.put(equipmentSlot, 0);
                    continue;
                }

                int warningTime = registeredWarningTimes.getOrDefault(equipmentSlot, 0);
                boolean check = false;
                if (!itemStack.getItem().equals(registeredItems.get(equipmentSlot))) {
                    check = true;
                } else if ((time - warningTime) / 1000 > AdditionalGenericConfig.DURABILITY_WARNING_COOLDOWN.getIntegerValue()) {
                    check = true;
                }
                if (!check) {
                    continue;
                }
                registeredItems.put(equipmentSlot, itemStack.getItem());
                registeredWarningTimes.put(equipmentSlot, time);
                EclipsesTweakerooUtil.showLowDurabilityWarning(itemStack, false);
            }
        }));

        ScreenEvents.AFTER_INIT.register(((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof DisconnectedScreen disconnectedScreen && AdditionalFeatureToggle.TWEAK_AUTO_RECONNECT.getBooleanValue()) {
                int disconnectedTime = EclipsesTweakerooUtil.milliTime();
                ButtonWidget backButton = (ButtonWidget) disconnectedScreen.children().stream().filter(child -> child instanceof ButtonWidget).findFirst().orElseThrow();
                int originalWidth = backButton.getWidth();
                if (originalWidth < 300) {
                    backButton.setWidth(300);
                    backButton.setX(backButton.getX() - (backButton.getWidth() - originalWidth) / 2);
                }

                ScreenEvents.afterRender(screen).register(((screenInstance, drawContext,
                        mouseX, mouseY, tickDelta) -> {
                    int passed = EclipsesTweakerooUtil.milliTime() - disconnectedTime;
                    int wait = AdditionalGenericConfig.RECONNECT_TIME.getIntegerValue();
                    if (passed > wait) {
                        ConnectScreen.connect(
                                ((DisconnectedScreenAccessor) disconnectedScreen).getParent(),
                                MinecraftClient.getInstance(),
                                lastConnection, lastConnectionInfo,
                                false);
                    }
                    backButton.setMessage(TO_MENU_TEXT.copy()
                            .append(Text.of(" (reconnecting in "))
                            .append(Text.literal((wait - passed) + "ms").formatted(Formatting.GREEN))
                            .append(Text.of(")")));
                }));
            }
        }));

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(
                new SimpleSynchronousResourceReloadListener() {
                    @Override
                    public Identifier getFabricId() {
                        return new Identifier(MOD_ID, FANCYNAME_EFFECT_MAP_PATH);
                    }

                    @Override
                    public void reload(ResourceManager manager) {
                        STATUS_EFFECT_CHARACTER_MAP.clear();
                        try {
                            Resource effectMapResource = manager.findResources(FANCYNAME_EFFECT_MAP_PATH, path -> path.getPath().endsWith(FANCYNAME_EFFECT_MAP_NAME)).values().stream().findFirst().orElseThrow();
                            try(InputStream effectMapInputStream = effectMapResource.getInputStream()) {
                                String effectMapJson = new String(effectMapInputStream.readAllBytes());
                                JsonObject effectMap = JsonParser.parseString(effectMapJson)
                                        .getAsJsonObject();
                                for (String statusEffectString : effectMap.keySet()) {
                                    StatusEffect statusEffect = Registries.STATUS_EFFECT.get(new Identifier(statusEffectString));
                                    if (statusEffect != null) {
                                        STATUS_EFFECT_CHARACTER_MAP.put(statusEffect, effectMap.get(statusEffectString).getAsString());
                                    }
                                }
                            }
                        } catch (Exception exception) {
                            LOGGER.error("Failed loading status effect character map!", exception);
                        }
                    }
                });
    }

    // TODO: implement with a custom event?
    public static void setLastConnection(ServerAddress lastConnection) {
        EclipsesTweakeroo.lastConnection = lastConnection;
    }

    public static void setLastConnectionInfo(ServerInfo lastConnectionInfo) {
        EclipsesTweakeroo.lastConnectionInfo = lastConnectionInfo;
    }

    private static boolean useCheck(PlayerEntity player, Hand hand) {
        if (AdditionalGenericConfig.TWEAK_DURABILITY_PREVENT_USE.getBooleanValue()
                && AdditionalFeatureToggle.TWEAK_DURABILITY_CHECK.getBooleanValue()
                && player.getStackInHand(hand).isDamageable()) {
            if (player.getStackInHand(hand).getDamage()
                    < player.getStackInHand(hand).getMaxDamage() - 1) {
                return true;
            }
            EclipsesTweakerooUtil.showLowDurabilityWarning(player.getStackInHand(hand), true);
            return false;
        }
        return true;
    }
}

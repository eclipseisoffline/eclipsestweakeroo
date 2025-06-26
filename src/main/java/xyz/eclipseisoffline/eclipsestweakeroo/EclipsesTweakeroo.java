package xyz.eclipseisoffline.eclipsestweakeroo;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.registry.Registry;
import fi.dy.masa.malilib.util.data.ModInfo;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import org.slf4j.Logger;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesConfigs;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesHotkeys;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.event.AttemptConnectionCallback;
import xyz.eclipseisoffline.eclipsestweakeroo.gui.EclipsesTweakerooConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.hotkeys.EclipsesKeybindProvider;
import xyz.eclipseisoffline.eclipsestweakeroo.mixin.entity.AllayInvoker;
import xyz.eclipseisoffline.eclipsestweakeroo.mixin.screen.DisconnectedScreenAccessor;
import xyz.eclipseisoffline.eclipsestweakeroo.util.EclipsesTweakerooUtil;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EclipsesTweakeroo implements ClientModInitializer {

    public static final String MOD_ID = "eclipsestweakeroo";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Map<ResourceKey<MobEffect>, String> STATUS_EFFECT_CHARACTER_MAP = new HashMap<>();

    private static final Component TO_MENU_TEXT = Component.translatable("gui.toMenu");
    private final Map<EquipmentSlot, Item> registeredItems = new HashMap<>();
    private final Map<EquipmentSlot, Integer> registeredWarningTimes = new HashMap<>();
    private ServerAddress lastConnection = null;
    private ServerData lastConnectionInfo = null;

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.putBlock(Blocks.BARRIER, ChunkSectionLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(Blocks.LIGHT, ChunkSectionLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(Blocks.STRUCTURE_VOID, ChunkSectionLayer.TRANSLUCENT);

        EclipsesTweakerooUtil.populateStatusEffectColorMap();

        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
            // Registering value change callbacks here to prevent crashes
            EclipsesTweaksConfig.TWEAK_CREATIVE_ELYTRA_FLIGHT.setValueChangeCallback(value -> {
                if (client.player == null) {
                    return;
                }
                if (value.getBooleanValue()) {
                    if (client.player.isFallFlying()) {
                        client.player.startFallFlying();
                    }
                } else {
                    client.player.stopFallFlying();
                }
            });

            EclipsesTweaksConfig.TWEAK_RENDER_OPERATOR_BLOCKS.setValueChangeCallback(value -> {
                LevelRenderer levelRenderer = client.levelRenderer;
                levelRenderer.allChanged();
            });
        }));

        ClientPreAttackCallback.EVENT.register(((client, player, clickCount) -> !useCheck(player, InteractionHand.MAIN_HAND)));
        UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> {
            if (!useCheck(player, hand)) {
                return InteractionResult.FAIL;
            } else if (EclipsesDisableConfig.DISABLE_BLOCK_USE.getBooleanValue()) {
                return InteractionResult.FAIL;
            } else if (EclipsesDisableConfig.DISABLE_BED_EXPLOSION.getBooleanValue()
                    && !world.dimensionType().bedWorks()
                    && world.getBlockState(hitResult.getBlockPos()).getBlock() instanceof BedBlock) {
                return InteractionResult.FAIL;
            }

            return InteractionResult.PASS;
        }));
        UseEntityCallback.EVENT.register(
                ((player, world, hand, entity, hitResult) -> {
                    if (!useCheck(player, hand)) {
                        return InteractionResult.FAIL;
                    }

                    if (EclipsesDisableConfig.DISABLE_ALLAY_USE.getBooleanValue()
                            && entity instanceof Allay allay) {
                        if (!player.getItemInHand(hand).isEmpty()) {
                            Item item = player.getItemInHand(hand).getItem();
                            if (((AllayInvoker) allay).invokeCanDuplicate() && item == Items.AMETHYST_SHARD) {
                                return InteractionResult.PASS;
                            }
                            return InteractionResult.FAIL;
                        }
                    }

                    return InteractionResult.PASS;
                }));
        UseItemCallback.EVENT.register(((player, world, hand) -> {
            ItemStack usedStack = player.getItemInHand(hand);
            if (!useCheck(player, hand)) {
                return InteractionResult.FAIL;
            } else if (EclipsesTweaksConfig.TWEAK_LODESTONE.getBooleanValue() && usedStack.has(DataComponents.LODESTONE_TRACKER)) {
                LodestoneTracker tracker = usedStack.get(DataComponents.LODESTONE_TRACKER);
                assert tracker != null;

                MutableComponent info = Component.empty().append(usedStack.getHoverName()).append(" has ");
                if (tracker.target().isPresent()) {
                    info.append(tracker.target().orElseThrow().pos().toShortString() + " in " + tracker.target().orElseThrow().dimension().location() + " as set position");
                } else {
                    info.append("no tracked position");
                }

                Minecraft.getInstance().gui.getChat().addMessage(info);
                return InteractionResult.CONSUME;
            }

            return InteractionResult.PASS;
        }));

        ClientTickEvents.START_WORLD_TICK.register((world -> {
            if (world.getGameTime() % 10 != 0 || !EclipsesTweaksConfig.TWEAK_DURABILITY_CHECK.getBooleanValue()) {
                return;
            }

            assert Minecraft.getInstance().player != null;
            int time = EclipsesTweakerooUtil.milliTime();
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack itemStack = Minecraft.getInstance().player.getItemBySlot(slot);

                if (!EclipsesTweakerooUtil.shouldWarnDurability(itemStack)) {
                    registeredWarningTimes.put(slot, 0);
                    continue;
                }

                int warningTime = registeredWarningTimes.getOrDefault(slot, 0);
                boolean check = false;
                if (!itemStack.getItem().equals(registeredItems.get(slot))) {
                    check = true;
                } else if ((time - warningTime) / 1000
                        > EclipsesGenericConfig.DURABILITY_WARNING_COOLDOWN.getIntegerValue()) {
                    check = true;
                }
                if (!check) {
                    continue;
                }
                registeredItems.put(slot, itemStack.getItem());
                registeredWarningTimes.put(slot, time);
                EclipsesTweakerooUtil.showLowDurabilityWarning(itemStack, false);
            }
        }));

        AttemptConnectionCallback.EVENT.register((address, connectionInfo) -> {
            lastConnection = address;
            lastConnectionInfo = connectionInfo;
        });
        ScreenEvents.AFTER_INIT.register(((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof DisconnectedScreen disconnectedScreen
                    && EclipsesTweaksConfig.TWEAK_AUTO_RECONNECT.getBooleanValue()) {
                int disconnectedTime = EclipsesTweakerooUtil.milliTime();
                Button backButton = (Button) disconnectedScreen.children().stream().filter(child -> child instanceof Button).findFirst().orElseThrow();
                int originalWidth = backButton.getWidth();
                if (originalWidth < 300) {
                    backButton.setWidth(300);
                    backButton.setX(
                            backButton.getX() - (backButton.getWidth() - originalWidth) / 2);
                }

                ScreenEvents.afterRender(screen).register(((screenInstance, drawContext,
                        mouseX, mouseY, tickDelta) -> {
                    int passed = EclipsesTweakerooUtil.milliTime() - disconnectedTime;
                    int wait = EclipsesGenericConfig.RECONNECT_TIME.getIntegerValue();
                    if (passed > wait) {
                        ConnectScreen.startConnecting(
                                ((DisconnectedScreenAccessor) disconnectedScreen).getParent(),
                                Minecraft.getInstance(),
                                lastConnection, lastConnectionInfo,
                                false, null);
                    }
                    backButton.setMessage(TO_MENU_TEXT.copy()
                            .append(Component.literal(" (reconnecting in "))
                            .append(Component.literal((wait - passed) + "ms")
                                    .withStyle(ChatFormatting.GREEN))
                            .append(Component.literal(")")));
                }));
            }
        }));

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(
                new SimpleSynchronousResourceReloadListener() {
                    @Override
                    public ResourceLocation getFabricId() {
                        return ResourceLocation.tryBuild(MOD_ID, "fancyname");
                    }

                    @Override
                    public void onResourceManagerReload(ResourceManager manager) {
                        STATUS_EFFECT_CHARACTER_MAP.clear();
                        try {
                            Resource effectMapResource = manager.getResourceOrThrow(ResourceLocation.tryBuild(MOD_ID, "fancyname/effect_map.json"));
                            try (InputStream effectMapInputStream = effectMapResource.open()) {
                                String effectMapJson = new String(effectMapInputStream.readAllBytes());
                                JsonObject effectMap = JsonParser.parseString(effectMapJson).getAsJsonObject();
                                for (String statusEffectString : effectMap.keySet()) {
                                    ResourceKey<MobEffect> statusEffect = ResourceKey.create(Registries.MOB_EFFECT, ResourceLocation.tryParse(statusEffectString));
                                    STATUS_EFFECT_CHARACTER_MAP.put(statusEffect, effectMap.get(statusEffectString).getAsString());
                                }
                            }
                        } catch (Exception exception) {
                            LOGGER.error("Failed loading status effect character map!", exception);
                        }
                    }
                });

        ConfigManager.getInstance().registerConfigHandler(MOD_ID, new EclipsesConfigs());

        Registry.CONFIG_SCREEN.registerConfigScreenFactory(new ModInfo(MOD_ID, "Eclipse's Tweakeroo", () -> new EclipsesTweakerooConfig(null)));

        EclipsesHotkeys.bootstrap();
        EclipsesKeybindProvider.bootstrap();
    }

    private static boolean useCheck(Player player, InteractionHand hand) {
        if (EclipsesDisableConfig.DISABLE_OFFHAND_USE.getBooleanValue()
                && hand == InteractionHand.OFF_HAND) {
            return false;
        }

        if (EclipsesGenericConfig.TWEAK_DURABILITY_PREVENT_USE.getBooleanValue()
                && EclipsesTweaksConfig.TWEAK_DURABILITY_CHECK.getBooleanValue()
                && player.getItemInHand(hand).isDamageableItem()) {
            if (player.getItemInHand(hand).getDamageValue() < player.getItemInHand(hand).getMaxDamage() - EclipsesGenericConfig.DURABILITY_PREVENT_USE_THRESHOLD.getIntegerValue()) {
                return true;
            }
            EclipsesTweakerooUtil.showLowDurabilityWarning(player.getItemInHand(hand), true);
            return false;
        }
        return true;
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.event;

import fi.dy.masa.tweakeroo.config.FeatureToggle;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesFixesConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.mixin.entity.AllayInvoker;
import xyz.eclipseisoffline.eclipsestweakeroo.mixin.screen.DisconnectedScreenAccessor;
import xyz.eclipseisoffline.eclipsestweakeroo.util.EclipsesTweakerooUtil;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

import java.util.HashMap;
import java.util.Map;

public class EclipsesListeners implements ClientLifecycleEvents.ClientStarted,
        ClientPlayConnectionEvents.Join,
        ClientPreAttackCallback, UseBlockCallback, UseEntityCallback, UseItemCallback,
        ClientTickEvents.StartWorldTick, AttemptConnectionCallback, ScreenEvents.AfterInit {

    private static final Component TO_SERVER_LIST = Component.translatable("gui.toMenu");

    private final Map<EquipmentSlot, Item> durabilityItems = new HashMap<>();
    private final Map<EquipmentSlot, Long> durabilityWarningTimes = new HashMap<>();
    private ServerAddress lastConnection = null;
    private ServerData lastConnectionInfo = null;
    
    @Override
    public void onStartTick(ClientLevel level) {
        if (level.getGameTime() % 10 != 0 || !ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_DURABILITY_CHECK)) {
            return;
        }

        assert Minecraft.getInstance().player != null;
        long time = System.currentTimeMillis();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack itemStack = Minecraft.getInstance().player.getItemBySlot(slot);

            if (!EclipsesTweakerooUtil.shouldWarnDurability(itemStack)) {
                durabilityWarningTimes.put(slot, 0L);
                continue;
            }

            long warningTime = durabilityWarningTimes.getOrDefault(slot, 0L);
            boolean check = false;
            if (!itemStack.getItem().equals(durabilityItems.get(slot))) {
                check = true;
            } else if ((time - warningTime) / 1000 > EclipsesGenericConfig.DURABILITY_WARNING_COOLDOWN.getIntegerValue()) {
                check = true;
            }
            if (!check) {
                continue;
            }

            durabilityItems.put(slot, itemStack.getItem());
            durabilityWarningTimes.put(slot, time);
            EclipsesTweakerooUtil.showLowDurabilityWarning(itemStack, false);
        }
    }

    @Override
    public void onClientStarted(Minecraft client) {
        // Registering value change callbacks here to prevent crashes
        EclipsesTweaksConfig.bootstrap(client);
    }

    @Override
    public void onPlayReady(ClientPacketListener listener, PacketSender sender, Minecraft client) {
        if (EclipsesFixesConfig.GAMMA_OVERRIDE_FIX.getBooleanValue()) {
            FeatureToggle.TWEAK_GAMMA_OVERRIDE.onValueChanged();
        }
    }

    @Override
    public void afterInit(Minecraft minecraft, Screen screen, int i, int i1) {
        if (screen instanceof DisconnectedScreen disconnectedScreen && ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_AUTO_RECONNECT)) {
            long disconnectedTime = System.currentTimeMillis();
            Button backButton = (Button) disconnectedScreen.children().stream().filter(child -> child instanceof Button).findFirst().orElseThrow();
            int originalWidth = backButton.getWidth();
            if (originalWidth < 300) {
                backButton.setWidth(300);
                backButton.setX(
                        backButton.getX() - (backButton.getWidth() - originalWidth) / 2);
            }

            ScreenEvents.afterRender(screen).register(((screenInstance, drawContext,
                                                        mouseX, mouseY, tickDelta) -> {
                long passed = System.currentTimeMillis() - disconnectedTime;
                int wait = EclipsesGenericConfig.RECONNECT_TIME.getIntegerValue();
                if (passed > wait) {
                    ConnectScreen.startConnecting(
                            ((DisconnectedScreenAccessor) disconnectedScreen).getParent(),
                            Minecraft.getInstance(),
                            lastConnection, lastConnectionInfo,
                            false);
                }
                backButton.setMessage(TO_SERVER_LIST.copy()
                        .append(Component.literal(" (reconnecting in "))
                        .append(Component.literal((wait - passed) + "ms")
                                .withStyle(ChatFormatting.GREEN))
                        .append(Component.literal(")")));
            }));
        }
    }

    @Override
    public boolean onClientPlayerPreAttack(Minecraft minecraft, LocalPlayer player, int clickCount) {
        return EclipsesTweakerooUtil.shouldDisableUse(player, InteractionHand.MAIN_HAND);
    }

    @Override
    public InteractionResult interact(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (EclipsesTweakerooUtil.shouldDisableUse(player, hand)) {
            return InteractionResult.FAIL;
        } else if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_BLOCK_USE)) {
            return InteractionResult.FAIL;
        } else if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_BED_EXPLOSION)
                && !level.dimensionType().bedWorks()
                && level.getBlockState(hitResult.getBlockPos()).getBlock() instanceof BedBlock) {
            return InteractionResult.FAIL;
        }

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult interact(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (EclipsesTweakerooUtil.shouldDisableUse(player, hand)) {
            return InteractionResult.FAIL;
        }

        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_ALLAY_USE) && entity instanceof Allay allay) {
            if (!player.getItemInHand(hand).isEmpty()) {
                Item item = player.getItemInHand(hand).getItem();
                if (((AllayInvoker) allay).invokeCanDuplicate() && item == Items.AMETHYST_SHARD) {
                    return InteractionResult.PASS;
                }
                return InteractionResult.FAIL;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> interact(Player player, Level level, InteractionHand hand) {
        ItemStack usedStack = player.getItemInHand(hand);
        if (EclipsesTweakerooUtil.shouldDisableUse(player, hand)) {
            return InteractionResultHolder.fail(usedStack);
        } else if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_LODESTONE) && usedStack.is(Items.COMPASS) && CompassItem.isLodestoneCompass(usedStack)) {
            assert usedStack.getTag() != null;
            GlobalPos target = CompassItem.getLodestonePosition(usedStack.getTag());

            MutableComponent info = Component.empty().append(usedStack.getHoverName()).append(" has ");
            if (target != null) {
                info.append(target.pos().toShortString() + " in " + target.dimension().location() + " as set position");
            } else {
                info.append("no tracked position");
            }

            Minecraft.getInstance().gui.getChat().addMessage(info);
            return InteractionResultHolder.consume(usedStack);
        }

        return InteractionResultHolder.pass(usedStack);
    }

    @Override
    public void connect(ServerAddress address, ServerData connectionInfo) {
        lastConnection = address;
        lastConnectionInfo = connectionInfo;
    }

    public static void bootstrap() {
        EclipsesListeners listeners = new EclipsesListeners();
        ClientTickEvents.START_WORLD_TICK.register(listeners);
        ClientLifecycleEvents.CLIENT_STARTED.register(listeners);
        ClientPlayConnectionEvents.JOIN.register(listeners);
        ScreenEvents.AFTER_INIT.register(listeners);
        ClientPreAttackCallback.EVENT.register(listeners);
        UseBlockCallback.EVENT.register(listeners);
        UseEntityCallback.EVENT.register(listeners);
        UseItemCallback.EVENT.register(listeners);
        AttemptConnectionCallback.EVENT.register(listeners);
    }
}

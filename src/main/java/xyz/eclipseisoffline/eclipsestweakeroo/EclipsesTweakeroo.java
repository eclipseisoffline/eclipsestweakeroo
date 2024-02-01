package xyz.eclipseisoffline.eclipsestweakeroo;

import fi.dy.masa.tweakeroo.config.FeatureToggle;
import java.util.HashMap;
import java.util.Map;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFixesConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.EclipsesTweakerooUtil;

public class EclipsesTweakeroo implements ClientModInitializer {
    private static final double DURABILITY_WARNING = 0.9;
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
        UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> useCheck(player, hand)
                ? ActionResult.PASS : ActionResult.FAIL));
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

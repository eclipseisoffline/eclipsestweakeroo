package xyz.eclipseisoffline.eclipsestweakeroo;

import fi.dy.masa.tweakeroo.config.FeatureToggle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFixesConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.EclipsesTweakerooUtil;

public class EclipsesTweakeroo implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
            if (AdditionalFixesConfig.GAMMA_OVERRIDE_FIX.getBooleanValue()) {
                FeatureToggle.TWEAK_GAMMA_OVERRIDE.onValueChanged();
            }
        }));

        AttackBlockCallback.EVENT.register(((player, world, hand, pos, direction) ->
                useCheck(player, hand) ? ActionResult.PASS : ActionResult.FAIL));
        AttackEntityCallback.EVENT.register(((player, world, hand, entity, hitResult) ->
                useCheck(player, hand) ? ActionResult.PASS : ActionResult.FAIL));
        UseBlockCallback.EVENT.register(((player, world, hand, hitResult) ->
                useCheck(player, hand) ? ActionResult.PASS : ActionResult.FAIL));
    }

    private boolean useCheck(PlayerEntity player, Hand hand) {
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

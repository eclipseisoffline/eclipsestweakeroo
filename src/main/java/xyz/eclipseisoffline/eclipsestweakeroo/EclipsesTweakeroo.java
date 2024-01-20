package xyz.eclipseisoffline.eclipsestweakeroo;

import fi.dy.masa.tweakeroo.config.FeatureToggle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFixesConfig;

public class EclipsesTweakeroo implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
            if (AdditionalFixesConfig.GAMMA_OVERRIDE_FIX.getBooleanValue()) {
                FeatureToggle.TWEAK_GAMMA_OVERRIDE.onValueChanged();
            }
        }));
    }
}

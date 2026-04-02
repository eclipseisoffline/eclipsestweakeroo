package xyz.eclipseisoffline.eclipsestweakeroo.client.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.effect.MobEffect;
import xyz.eclipseisoffline.eclipsestweakeroo.EclipsesTweakeroo;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class StatusEffectCharacterLoader implements ResourceManagerReloadListener {
    public static final Map<ResourceKey<MobEffect>, String> MAP = new HashMap<>();

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        MAP.clear();
        try {
            Resource effectMapResource = manager.getResourceOrThrow(EclipsesTweakeroo.getModdedIdentifier("fancyname/effect_map.json"));
            try (InputStream effectMapInputStream = effectMapResource.open()) {
                String effectMapJson = new String(effectMapInputStream.readAllBytes());
                JsonObject effectMap = JsonParser.parseString(effectMapJson).getAsJsonObject();

                for (String statusEffectString : effectMap.keySet()) {
                    ResourceKey<MobEffect> statusEffect = ResourceKey.create(Registries.MOB_EFFECT, Identifier.parse(statusEffectString));
                    MAP.put(statusEffect, effectMap.get(statusEffectString).getAsString());
                }
            }
        } catch (Exception exception) {
            EclipsesTweakeroo.LOGGER.error("Failed loading status effect character map!", exception);
        }
    }

    public static void bootstrap() {
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(EclipsesTweakeroo.getModdedIdentifier("status_effect_character_loader"), new StatusEffectCharacterLoader());
    }
}

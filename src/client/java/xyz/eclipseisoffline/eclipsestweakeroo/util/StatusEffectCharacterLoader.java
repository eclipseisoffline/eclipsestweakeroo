package xyz.eclipseisoffline.eclipsestweakeroo.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.effect.MobEffect;
import org.jetbrains.annotations.NotNull;
import xyz.eclipseisoffline.eclipsestweakeroo.EclipsesTweakeroo;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class StatusEffectCharacterLoader implements SimpleSynchronousResourceReloadListener{
    public static final Map<ResourceKey<MobEffect>, String> MAP = new HashMap<>();

    @Override
    public ResourceLocation getFabricId() {
        return EclipsesTweakeroo.getModdedLocation("status_effect_character_loader");
    }

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager manager) {
        MAP.clear();
        try {
            Resource effectMapResource = manager.getResourceOrThrow(EclipsesTweakeroo.getModdedLocation("fancyname/effect_map.json"));
            try (InputStream effectMapInputStream = effectMapResource.open()) {
                String effectMapJson = new String(effectMapInputStream.readAllBytes());
                JsonObject effectMap = JsonParser.parseString(effectMapJson).getAsJsonObject();

                for (String statusEffectString : effectMap.keySet()) {
                    ResourceKey<MobEffect> statusEffect = ResourceKey.create(Registries.MOB_EFFECT, ResourceLocation.parse(statusEffectString));
                    MAP.put(statusEffect, effectMap.get(statusEffectString).getAsString());
                }
            }
        } catch (Exception exception) {
            EclipsesTweakeroo.LOGGER.error("Failed loading status effect character map!", exception);
        }
    }

    public static void bootstrap() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new StatusEffectCharacterLoader());
    }
}

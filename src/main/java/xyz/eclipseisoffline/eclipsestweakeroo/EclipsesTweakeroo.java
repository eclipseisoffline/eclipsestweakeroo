package xyz.eclipseisoffline.eclipsestweakeroo;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import xyz.eclipseisoffline.eclipsestweakeroo.network.EclipsesTweakerooNetworking;
import xyz.eclipseisoffline.eclipsestweakeroo.toggle.ToggleConfig;

public class EclipsesTweakeroo implements ModInitializer, DedicatedServerModInitializer {

    public static final String MOD_ID = "eclipsestweakeroo";
    public static final String MOD_NAME = "Eclipse's Tweakeroo Additions";
    public static final String MOD_NAME_SHORT = "Eclipse's Tweakeroo";
    public static final String MOD_VERSION = FabricLoader.getInstance()
            .getModContainer(MOD_ID).orElseThrow()
            .getMetadata().getVersion().getFriendlyString();

    public static final Logger LOGGER = LogUtils.getLogger();

    private static ToggleConfig config = ToggleConfig.EMPTY;

    @Override
    public void onInitialize() {
        EclipsesTweakerooNetworking.bootstrap();
    }

    @Override
    public void onInitializeServer() {
        config = ToggleConfig.read();
        config.save();
    }

    public static ToggleConfig getConfig() {
        return config;
    }

    public static ResourceLocation getModdedLocation(String path) {
        return ResourceLocation.tryBuild(MOD_ID, path);
    }
}

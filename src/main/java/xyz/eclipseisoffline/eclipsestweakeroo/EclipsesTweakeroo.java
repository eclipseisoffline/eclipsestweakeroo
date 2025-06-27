package xyz.eclipseisoffline.eclipsestweakeroo;

import com.mojang.logging.LogUtils;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.registry.Registry;
import fi.dy.masa.malilib.util.data.ModInfo;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import org.slf4j.Logger;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesConfigs;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesHotkeys;
import xyz.eclipseisoffline.eclipsestweakeroo.event.EclipsesListeners;
import xyz.eclipseisoffline.eclipsestweakeroo.gui.EclipsesTweakerooConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.hotkeys.EclipsesKeybindProvider;
import xyz.eclipseisoffline.eclipsestweakeroo.util.EclipsesTweakerooUtil;
import xyz.eclipseisoffline.eclipsestweakeroo.util.StatusEffectCharacterLoader;

public class EclipsesTweakeroo implements ClientModInitializer {

    public static final String MOD_ID = "eclipsestweakeroo";
    public static final String MOD_NAME = "Eclipse's Tweakeroo Additions";
    public static final String MOD_NAME_SHORT = "Eclipse's Tweakeroo";
    public static final String MOD_VERSION = FabricLoader.getInstance()
            .getModContainer(EclipsesTweakeroo.MOD_ID).orElseThrow()
            .getMetadata().getVersion().getFriendlyString();

    public static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitializeClient() {
        ConfigManager.getInstance().registerConfigHandler(MOD_ID, new EclipsesConfigs());
        Registry.CONFIG_SCREEN.registerConfigScreenFactory(new ModInfo(MOD_ID, MOD_NAME_SHORT, () -> new EclipsesTweakerooConfig(null)));

        EclipsesHotkeys.bootstrap();
        EclipsesKeybindProvider.bootstrap();

        EclipsesListeners.bootstrap();
        StatusEffectCharacterLoader.bootstrap();
        EclipsesTweakerooUtil.populateStatusEffectColorMap();

        BlockRenderLayerMap.putBlock(Blocks.BARRIER, ChunkSectionLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(Blocks.LIGHT, ChunkSectionLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(Blocks.STRUCTURE_VOID, ChunkSectionLayer.TRANSLUCENT);
    }

    public static ResourceLocation getModdedLocation(String path) {
        return ResourceLocation.tryBuild(MOD_ID, path);
    }
}

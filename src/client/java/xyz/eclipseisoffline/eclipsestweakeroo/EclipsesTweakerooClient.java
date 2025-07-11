package xyz.eclipseisoffline.eclipsestweakeroo;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.registry.Registry;
import fi.dy.masa.malilib.util.data.ModInfo;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.level.block.Blocks;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesConfigs;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesHotkeys;
import xyz.eclipseisoffline.eclipsestweakeroo.event.EclipsesListeners;
import xyz.eclipseisoffline.eclipsestweakeroo.gui.EclipsesTweakerooConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.hotkeys.EclipsesKeybindProvider;
import xyz.eclipseisoffline.eclipsestweakeroo.network.EclipsesTweakerooClientNetworking;
import xyz.eclipseisoffline.eclipsestweakeroo.util.EclipsesTweakerooUtil;
import xyz.eclipseisoffline.eclipsestweakeroo.util.StatusEffectCharacterLoader;

public class EclipsesTweakerooClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ConfigManager.getInstance().registerConfigHandler(EclipsesTweakeroo.MOD_ID, new EclipsesConfigs());
        Registry.CONFIG_SCREEN.registerConfigScreenFactory(new ModInfo(EclipsesTweakeroo.MOD_ID, EclipsesTweakeroo.MOD_NAME_SHORT, () -> new EclipsesTweakerooConfig(null)));

        EclipsesHotkeys.bootstrap();
        EclipsesKeybindProvider.bootstrap();

        EclipsesListeners.bootstrap();
        StatusEffectCharacterLoader.bootstrap();
        EclipsesTweakerooClientNetworking.bootstrap();
        EclipsesTweakerooUtil.populateStatusEffectColorMap();

        BlockRenderLayerMap.putBlock(Blocks.BARRIER, ChunkSectionLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(Blocks.LIGHT, ChunkSectionLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(Blocks.STRUCTURE_VOID, ChunkSectionLayer.TRANSLUCENT);
    }
}

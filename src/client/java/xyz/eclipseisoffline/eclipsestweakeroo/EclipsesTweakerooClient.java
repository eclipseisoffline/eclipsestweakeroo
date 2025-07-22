package xyz.eclipseisoffline.eclipsestweakeroo;

import fi.dy.masa.malilib.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Blocks;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesConfigs;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesHotkeys;
import xyz.eclipseisoffline.eclipsestweakeroo.event.EclipsesListeners;
import xyz.eclipseisoffline.eclipsestweakeroo.hotkeys.EclipsesKeybindProvider;
import xyz.eclipseisoffline.eclipsestweakeroo.network.EclipsesTweakerooClientNetworking;
import xyz.eclipseisoffline.eclipsestweakeroo.util.EclipsesTweakerooUtil;
import xyz.eclipseisoffline.eclipsestweakeroo.util.StatusEffectCharacterLoader;

public class EclipsesTweakerooClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ConfigManager.getInstance().registerConfigHandler(EclipsesTweakeroo.MOD_ID, new EclipsesConfigs());

        EclipsesHotkeys.bootstrap();
        EclipsesKeybindProvider.bootstrap();

        EclipsesListeners.bootstrap();
        StatusEffectCharacterLoader.bootstrap();
        EclipsesTweakerooClientNetworking.bootstrap();
        EclipsesTweakerooUtil.populateStatusEffectColorMap();

        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.BARRIER, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.LIGHT, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.STRUCTURE_VOID, RenderType.translucent());
    }
}

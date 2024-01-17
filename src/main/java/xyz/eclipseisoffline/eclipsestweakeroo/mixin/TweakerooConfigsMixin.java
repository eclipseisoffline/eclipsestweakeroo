package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import static fi.dy.masa.tweakeroo.config.Configs.Disable.*;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.tweakeroo.config.Configs.Disable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;

@Mixin(Disable.class)
public class TweakerooConfigsMixin {
    @Mutable
    @Final
    @Shadow(remap = false)
    public static ImmutableList<IHotkeyTogglable> OPTIONS;

    @Inject(method = "Lfi/dy/masa/tweakeroo/config/Configs$Disable;<clinit>()V", at = @At("TAIL"))
    private static void staticInit(CallbackInfo callbackInfo) {
        OPTIONS = ImmutableList.of(
                DISABLE_ARMOR_STAND_RENDERING,
                DISABLE_AXE_STRIPPING,
                DISABLE_BAT_SPAWNING,
                DISABLE_BEACON_BEAM_RENDERING,
                DISABLE_BLOCK_BREAK_PARTICLES,
                DISABLE_DOUBLE_TAP_SPRINT,
                DISABLE_BOSS_BAR,
                DISABLE_BOSS_FOG,
                DISABLE_CHUNK_RENDERING,
                DISABLE_CLIENT_ENTITY_UPDATES,
                DISABLE_CLIENT_LIGHT_UPDATES,
                DISABLE_CONSTANT_CHUNK_SAVING,
                DISABLE_CREATIVE_INFESTED_BLOCKS,
                DISABLE_DEAD_MOB_RENDERING,
                DISABLE_DEAD_MOB_TARGETING,
                DISABLE_ENTITY_RENDERING,
                DISABLE_ENTITY_TICKING,
                DISABLE_FALLING_BLOCK_RENDER,
                DISABLE_FP_EFFECT_PARTICLES,
                DISABLE_INVENTORY_EFFECTS,
                DISABLE_ITEM_SWITCH_COOLDOWN,
                DISABLE_MOB_SPAWNER_MOB_RENDER,
                DISABLE_NAUSEA_EFFECT,
                DISABLE_NETHER_FOG,
                DISABLE_NETHER_PORTAL_SOUND,
                DISABLE_OBSERVER,
                DISABLE_OFFHAND_RENDERING,
                DISABLE_PARTICLES,
                DISABLE_PORTAL_GUI_CLOSING,
                DISABLE_RAIN_EFFECTS,
                DISABLE_RENDERING_SCAFFOLDING,
                DISABLE_RENDER_DISTANCE_FOG,
                DISABLE_SCOREBOARD_RENDERING,
                DISABLE_SHULKER_BOX_TOOLTIP,
                DISABLE_SHOVEL_PATHING,
                DISABLE_SIGN_GUI,
                DISABLE_SKY_DARKNESS,
                DISABLE_SLIME_BLOCK_SLOWDOWN,
                DISABLE_STATUS_EFFECT_HUD,
                DISABLE_TILE_ENTITY_RENDERING,
                DISABLE_TILE_ENTITY_TICKING,
                DISABLE_VILLAGER_TRADE_LOCKING,
                DISABLE_WALL_UNSPRINT,
                DISABLE_WORLD_VIEW_BOB,
                AdditionalDisableConfig.DISABLE_ENTITY_COLLISIONS
        );
    }
}

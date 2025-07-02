package xyz.eclipseisoffline.eclipsestweakeroo.mixin.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.gui.contextualbar.ContextualBarRenderer;
import net.minecraft.client.gui.contextualbar.LocatorBarRenderer;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.waypoints.TrackedWaypoint;
import net.minecraft.world.waypoints.WaypointTransmitter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

@Mixin(LocatorBarRenderer.class)
public abstract class LocatorBarRendererMixin implements ContextualBarRenderer {

    @Unique
    private static final int MIN_FACE_SIZE = 5;
    @Unique
    private static final int MAX_FACE_WIDTH = 10;
    @Unique
    private static final float FACE_SIZE_LERP = (float) (MIN_FACE_SIZE - MAX_FACE_WIDTH) / WaypointTransmitter.REALLY_FAR_DISTANCE;

    @Shadow
    @Final
    private Minecraft minecraft;

    @WrapOperation(method = "method_70870", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/ResourceLocation;IIIII)V"))
    public void usePlayerIcon(GuiGraphics instance, RenderPipeline pipeline, ResourceLocation texture, int x, int y, int width, int height, int color, Operation<Void> original,
                              @Local(argsOnly = true) TrackedWaypoint waypoint, @Local float distance) {
        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_LOCATOR_BAR) && waypoint.id().left().isPresent()) {
            PlayerInfo player = minecraft.getConnection().getPlayerInfo(waypoint.id().left().orElseThrow());
            if (player != null) {
                float size = MAX_FACE_WIDTH + FACE_SIZE_LERP * distance;
                if (size < MIN_FACE_SIZE) {
                    size = MIN_FACE_SIZE;
                }

                PlayerFaceRenderer.draw(instance, player.getSkin().texture(), x + (int) ((width - size) / 2), y + (int) ((height - size) / 2), (int) size,
                        player.showHat(), false, -1);
                return;
            }
        }
        original.call(instance, pipeline, texture, x, y, width, height, color);
    }
}

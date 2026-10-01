package xyz.eclipseisoffline.eclipsestweakeroo.client.mixin.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.happyghast.HappyGhast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import xyz.eclipseisoffline.eclipsestweakeroo.client.config.EclipsesGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.client.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.client.util.ToggleManager;

import java.util.Objects;

@Mixin(LevelExtractor.class)
public abstract class LevelExtractorMixin implements ResourceManagerReloadListener, AutoCloseable {

    @WrapOperation(method = "extractVisibleEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/extract/LevelExtractor;isEntityVisible(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/culling/Frustum;DDDFJ)Z"))
    public boolean noRenderIfHappyGhastTweak(LevelExtractor instance, Entity entity, Frustum frustum, double camX, double camY, double camZ, float partialTicks, long chunkFadeDuration, Operation<Boolean> original) {
        if (shouldHideHappyGhast(entity)) {
            return false;
        }
        return original.call(instance, entity, frustum, camX, camY, camZ, partialTicks, chunkFadeDuration);
    }

    @Unique
    private static boolean shouldHideHappyGhast(Entity entity) {
        return ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_HAPPY_GHAST)
                && EclipsesGenericConfig.HIDE_HAPPY_GHAST.getBooleanValue()
                && entity instanceof HappyGhast
                && Objects.requireNonNull(Minecraft.getInstance().player).getControlledVehicle() == entity;
    }
}

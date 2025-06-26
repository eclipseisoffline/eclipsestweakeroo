package xyz.eclipseisoffline.eclipsestweakeroo.mixin.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.HappyGhast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;

import java.util.Objects;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin implements ResourceManagerReloadListener, AutoCloseable {


    @WrapOperation(method = "collectVisibleEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;shouldRender(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/culling/Frustum;DDD)Z"))
    public boolean noRenderIfHappyGhastTweak(EntityRenderDispatcher instance, Entity entity, Frustum frustum, double camX, double camY, double camZ, Operation<Boolean> original) {
        if (shouldHideHappyGhast(entity)) {
            return false;
        }
        return original.call(instance, entity, frustum, camX, camY, camZ);
    }

    @WrapOperation(method = "collectVisibleEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hasIndirectPassenger(Lnet/minecraft/world/entity/Entity;)Z"))
    public boolean noRenderIfHappyGhastTweak(Entity instance, Entity entity, Operation<Boolean> original) {
        if (shouldHideHappyGhast(instance)) {
            return false;
        }
        return original.call(instance, entity);
    }

    @Unique
    private static boolean shouldHideHappyGhast(Entity entity) {
        return EclipsesTweaksConfig.TWEAK_CREATIVE_HAPPY_GHAST_FLIGHT.getBooleanValue()
                && entity instanceof HappyGhast
                && Objects.requireNonNull(Minecraft.getInstance().player).getControlledVehicle() == entity;
    }
}

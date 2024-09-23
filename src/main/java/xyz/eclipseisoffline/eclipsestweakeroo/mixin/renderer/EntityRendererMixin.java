package xyz.eclipseisoffline.eclipsestweakeroo.mixin.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.util.FancyName;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {

    @ModifyArg(method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;renderNameTag(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/network/chat/Component;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IF)V"), index = 1)
    public Component getFancyDisplayName(Entity entity, Component displayName, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float partialTick) {
        if (entity instanceof Player player) {
            if (AdditionalFeatureToggle.TWEAK_PLAYER_NAME.getBooleanValue()) {
                if (Minecraft.getInstance().getConnection() == null) {
                    return displayName;
                }

                PlayerInfo playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(entity.getUUID());
                return FancyName.applyFancyName(player, playerInfo);
            }
        } else if (AdditionalFeatureToggle.TWEAK_MOB_NAMES.getBooleanValue()
                && (entity instanceof LivingEntity living)) {
            return FancyName.applyFancyName(living, null);
        }

        return displayName;
    }
}

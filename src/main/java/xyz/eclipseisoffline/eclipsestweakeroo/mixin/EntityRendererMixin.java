package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.util.FancyName;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @ModifyArg(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/EntityRenderer;renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"), index = 1)
    public Text getDisplayName(Entity entity, Text text, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light) {
        MutableText name = (MutableText) text;

        if (entity instanceof PlayerEntity) {
            if (AdditionalFeatureToggle.TWEAK_PLAYER_NAME.getBooleanValue()) {
                if (MinecraftClient.getInstance().getNetworkHandler() == null) {
                    return name;
                }

                PlayerListEntry playerListEntry = MinecraftClient.getInstance().getNetworkHandler()
                        .getPlayerListEntry(entity.getUuid());
                return FancyName.applyFancyName((LivingEntity) entity, playerListEntry);
            }
        } else if (AdditionalFeatureToggle.TWEAK_MOB_NAMES.getBooleanValue()
                && (entity instanceof LivingEntity)) {
            return FancyName.applyFancyName((LivingEntity) entity, null);
        }

        return name;
    }
}

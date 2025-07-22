package xyz.eclipseisoffline.eclipsestweakeroo.mixin.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.FancyName;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getDisplayName()Lnet/minecraft/network/chat/Component;"))
    public Component getFancyDisplayName(Entity entity, Operation<Component> original) {
        if (entity instanceof Player player) {
            if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_PLAYER_NAME)) {
                if (Minecraft.getInstance().getConnection() != null) {
                    PlayerInfo playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(entity.getUUID());
                    return FancyName.applyFancyName(player, playerInfo);
                }
            }
        } else if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_MOB_NAMES) && (entity instanceof LivingEntity living)) {
            return FancyName.applyFancyName(living, null);
        }
        return original.call(entity);
    }
}

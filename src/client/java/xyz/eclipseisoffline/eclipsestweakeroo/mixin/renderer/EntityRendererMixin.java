package xyz.eclipseisoffline.eclipsestweakeroo.mixin.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.FancyName;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {

    @Inject(method = "getNameTag", at = @At("HEAD"), cancellable = true)
    public void getFancyDisplayName(T entity, CallbackInfoReturnable<Component> callbackInfoReturnable) {
        if (entity instanceof Player player) {
            if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_PLAYER_NAME)) {
                if (Minecraft.getInstance().getConnection() != null) {
                    PlayerInfo playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(entity.getUUID());
                    callbackInfoReturnable.setReturnValue(FancyName.applyFancyName(player, playerInfo));
                }
            }
        } else if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_MOB_NAMES)
                && (entity instanceof LivingEntity living)) {
            callbackInfoReturnable.setReturnValue(FancyName.applyFancyName(living, null));
        }
    }
}

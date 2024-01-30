package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setPosition(DDD)V"))
    public void setPosition(PlayerEntity instance, double x, double y, double z) {
        if (AdditionalDisableConfig.DISABLE_WORLD_BORDER.getBooleanValue()) {
            return;
        }
        instance.setPosition(x, y, z);
    }
}

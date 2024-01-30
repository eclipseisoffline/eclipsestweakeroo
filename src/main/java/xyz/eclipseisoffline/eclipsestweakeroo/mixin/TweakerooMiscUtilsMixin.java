package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import fi.dy.masa.tweakeroo.util.MiscUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalGenericConfig;

@Mixin(MiscUtils.class)
public class TweakerooMiscUtilsMixin {

    @Redirect(method = "commandNearbyPets", at = @At(value = "INVOKE", target = "Lfi/dy/masa/tweakeroo/util/MiscUtils;rightClickEntity(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/entity/player/PlayerEntity;)V"))
    private static void rightClickEntity(Entity entity, MinecraftClient mc, PlayerEntity player) {
        assert entity instanceof TameableEntity;
        if (((TameableEntity) entity).isBaby()
                && AdditionalGenericConfig.COMMAND_ONLY_ADULT_PETS.getBooleanValue()) {
            return;
        }
        MiscUtils.rightClickEntity(entity, mc, player);
    }
}

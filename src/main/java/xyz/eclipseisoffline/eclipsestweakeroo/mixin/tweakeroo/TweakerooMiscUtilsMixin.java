package xyz.eclipseisoffline.eclipsestweakeroo.mixin.tweakeroo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fi.dy.masa.tweakeroo.util.MiscUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesGenericConfig;

@Mixin(MiscUtils.class)
public abstract class TweakerooMiscUtilsMixin {

    @WrapOperation(method = "commandNearbyPets", at = @At(value = "INVOKE", target = "Lfi/dy/masa/tweakeroo/util/MiscUtils;rightClickEntity(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/Minecraft;Lnet/minecraft/world/entity/player/Player;)V"))
    private static void commandOnlyAdultPets(Entity entity, Minecraft mc, Player player, Operation<Void> original) {
        if (entity instanceof TamableAnimal tamable && tamable.isBaby()
                && EclipsesGenericConfig.COMMAND_ONLY_ADULT_PETS.getBooleanValue()) {
            return;
        }
        original.call(entity, mc, player);
    }
}

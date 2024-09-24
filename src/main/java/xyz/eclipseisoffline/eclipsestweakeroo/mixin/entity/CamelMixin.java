package xyz.eclipseisoffline.eclipsestweakeroo.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;

@Mixin(Camel.class)
public abstract class CamelMixin extends AbstractHorse implements PlayerRideableJumping, Saddleable {

    protected CamelMixin(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(method = "onPlayerJump", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/animal/camel/Camel;dashCooldown:I", opcode = Opcodes.GETFIELD))
    public int disableCooldown(int original) {
        if (AdditionalDisableConfig.DISABLE_HORSE_JUMP_CHARGE.getBooleanValue()) {
            return 0;
        }
        return original;
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

@Mixin(Camel.class)
public abstract class CamelMixin extends AbstractHorse {

    protected CamelMixin(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(method = "onPlayerJump", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/animal/camel/Camel;dashCooldown:I", opcode = Opcodes.GETFIELD))
    public int disableCooldown(int original) {
        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_HORSE_JUMP_CHARGE)) {
            return 0;
        }
        return original;
    }
}

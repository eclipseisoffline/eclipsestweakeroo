package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.particle.EntityEffectParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityEffectParticleEffect.class)
public interface EntityEffectParticleEffectAccessor {

    @Accessor
    int getColor();
}

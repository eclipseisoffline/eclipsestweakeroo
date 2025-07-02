package xyz.eclipseisoffline.eclipsestweakeroo.mixin.entity;

import java.util.List;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {

    @Accessor("DATA_EFFECT_PARTICLES")
    static EntityDataAccessor<List<ParticleOptions>> getDataEffectParticles() {
        throw new AssertionError();
    }
}

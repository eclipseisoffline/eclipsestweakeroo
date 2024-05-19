package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import java.util.List;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {

    @Accessor("POTION_SWIRLS")
    static TrackedData<List<ParticleEffect>> getTrackedPotionSwirls() {
        throw new AssertionError();
    }
}

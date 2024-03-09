package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.entity.passive.AllayEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AllayEntity.class)
public interface AllayEntityInvoker {

    @Invoker
    boolean invokeCanDuplicate();
}

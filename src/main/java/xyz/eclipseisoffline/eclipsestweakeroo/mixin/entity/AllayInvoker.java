package xyz.eclipseisoffline.eclipsestweakeroo.mixin.entity;

import net.minecraft.world.entity.animal.allay.Allay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Allay.class)
public interface AllayInvoker {

    @Invoker
    boolean invokeCanDuplicate();
}

package xyz.eclipseisoffline.eclipsestweakeroo.mixin.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.StructureVoidBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

@Mixin(StructureVoidBlock.class)
public abstract class StructureVoidBlockMixin extends Block {

    public StructureVoidBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "getRenderShape", at = @At("HEAD"), cancellable = true)
    public void useModelRenderShape(BlockState state, CallbackInfoReturnable<RenderShape> callbackInfoReturnable) {
        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_RENDER_OPERATOR_BLOCKS)) {
            callbackInfoReturnable.setReturnValue(RenderShape.MODEL);
        }
    }
}

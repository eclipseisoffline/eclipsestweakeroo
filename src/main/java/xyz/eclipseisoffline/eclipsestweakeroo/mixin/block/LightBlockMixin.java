package xyz.eclipseisoffline.eclipsestweakeroo.mixin.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;

@Mixin(LightBlock.class)
public abstract class LightBlockMixin extends Block implements SimpleWaterloggedBlock {

    public LightBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "getRenderShape", at = @At("HEAD"), cancellable = true)
    public void useModelRenderShape(BlockState state, CallbackInfoReturnable<RenderShape> callbackInfoReturnable) {
        if (EclipsesTweaksConfig.TWEAK_RENDER_OPERATOR_BLOCKS.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(RenderShape.MODEL);
        }
    }
}

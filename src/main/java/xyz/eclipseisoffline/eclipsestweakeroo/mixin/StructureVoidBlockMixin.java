package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.StructureVoidBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;

@Mixin(StructureVoidBlock.class)
public abstract class StructureVoidBlockMixin extends Block {

    public StructureVoidBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    public void setModelRenderType(
            BlockState state, CallbackInfoReturnable<BlockRenderType> callbackInfoReturnable) {
        if (AdditionalFeatureToggle.TWEAK_RENDER_OPERATOR_BLOCKS.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(BlockRenderType.MODEL);
        }
    }
}

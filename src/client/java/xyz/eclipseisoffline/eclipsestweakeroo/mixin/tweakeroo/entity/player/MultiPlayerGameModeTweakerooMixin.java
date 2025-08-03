package xyz.eclipseisoffline.eclipsestweakeroo.mixin.tweakeroo.entity.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToolSwitchManager;

@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeTweakerooMixin {
    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    private boolean isDestroying;

    @Unique
    private int lastBlockDestroyTick = -1;

    @Inject(method = "destroyBlock", at = @At("HEAD"))
    public void resetLastBlockDestroyTick(BlockPos pos, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        lastBlockDestroyTick = 10;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tickLastBlockDestroy(CallbackInfo callbackInfo) {
        if (isDestroying) {
            lastBlockDestroyTick = 10;
        } else if (lastBlockDestroyTick >= 0) {
            lastBlockDestroyTick--;
            if (lastBlockDestroyTick == -1) {
                ToolSwitchManager.revertCache(minecraft);
            }
        }
    }
}

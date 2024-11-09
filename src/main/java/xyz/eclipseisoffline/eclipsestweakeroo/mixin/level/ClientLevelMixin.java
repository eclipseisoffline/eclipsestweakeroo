package xyz.eclipseisoffline.eclipsestweakeroo.mixin.level;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;

import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level {

    protected ClientLevelMixin(WritableLevelData writableLevelData, ResourceKey<Level> resourceKey, RegistryAccess registryAccess, Holder<DimensionType> holder, boolean bl, boolean bl2, long l, int i) {
        super(writableLevelData, resourceKey, registryAccess, holder, bl, bl2, l, i);
    }

    @Inject(method = "getMarkerParticleTarget", at = @At("HEAD"), cancellable = true)
    public void noBlockParticles(CallbackInfoReturnable<Block> callbackInfoReturnable) {
        if (AdditionalFeatureToggle.TWEAK_RENDER_OPERATOR_BLOCKS.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(null);
        }
    }
}

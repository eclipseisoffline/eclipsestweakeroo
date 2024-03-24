package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World {

    protected ClientWorldMixin(MutableWorldProperties properties,
            RegistryKey<World> registryRef,
            DynamicRegistryManager registryManager,
            RegistryEntry<DimensionType> dimensionEntry,
            Supplier<Profiler> profiler,
            boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient,
                debugWorld,
                biomeAccess, maxChainedNeighborUpdates);
    }

    @Inject(method = "getBlockParticle", at = @At("HEAD"), cancellable = true)
    public void noBlockParticles(CallbackInfoReturnable<Block> callbackInfoReturnable) {
        if (AdditionalFeatureToggle.TWEAK_RENDER_OPERATOR_BLOCKS.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(null);
        }
    }
}

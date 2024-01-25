package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.util.EclipsesTweakerooUtil;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Unique
    private final double[] WARNINGS = {0.85, 0.9, 0.95};

    @Shadow public abstract boolean isDamageable();

    @Shadow public abstract int getDamage();

    @Inject(method = "setDamage", at = @At("HEAD"))
    public void damage(int damage, CallbackInfo callbackInfo) {
        if (!isDamageable() || damage == 0 || damage == getDamage()
                || MinecraftClient.getInstance() == null
                || MinecraftClient.getInstance().player == null
                || !AdditionalFeatureToggle.TWEAK_DURABILITY_CHECK.getBooleanValue()) {
            return;
        }

        MinecraftClient.getInstance().player.getItemsEquipped().forEach(itemStack -> {
            if (ItemStack.areItemsEqual(itemStack, (ItemStack) (Object) this)) {
                for (double warning : WARNINGS) {
                    int requiredDamage = (int) (warning * itemStack.getMaxDamage());
                    if (itemStack.getDamage() == requiredDamage) {
                        EclipsesTweakerooUtil.showLowDurabilityWarning(itemStack, false);
                    }
                }
            }
        });
    }
}

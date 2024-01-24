package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import fi.dy.masa.malilib.gui.Message.MessageType;
import fi.dy.masa.malilib.util.InfoUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract boolean isDamageable();

    @Inject(method = "setDamage", at = @At("TAIL"))
    public void damage(int damage, CallbackInfo callbackInfo) {
        if (!isDamageable() || damage == 0
                || MinecraftClient.getInstance() == null
                || MinecraftClient.getInstance().player == null
                || !AdditionalFeatureToggle.TWEAK_DURABILITY_CHECK.getBooleanValue()) {
            return;
        }

        MinecraftClient.getInstance().player.getItemsEquipped().forEach(itemStack -> {
            if (ItemStack.areItemsEqual(itemStack, (ItemStack) (Object) this)) {
                if ((double) itemStack.getDamage() / itemStack.getMaxDamage() > 0.9) {
                    InfoUtils.showGuiOrActionBarMessage(MessageType.WARNING, itemStack.getName().getString() + " is at low durability! "
                            + (itemStack.getMaxDamage() - itemStack.getDamage() - 1) + "/" + itemStack.getMaxDamage());
                }
            }
        });
    }
}

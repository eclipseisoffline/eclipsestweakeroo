package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;

@Mixin(targets = "net.minecraft.screen.PlayerScreenHandler$1")
public abstract class PlayerScreenHandlerEquipmentSlotMixin {

    @Inject(method = "canInsert", at = @At("HEAD"), cancellable = true)
    public void canAlwaysInsert(ItemStack stack,
            CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (AdditionalDisableConfig.DISABLE_EQUIPMENT_RESTRICTION.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(true);
        }
    }

    @Redirect(method = "canTakeItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;hasBindingCurse(Lnet/minecraft/item/ItemStack;)Z"))
    public boolean disableBindingCurse(ItemStack stack) {
        if (AdditionalDisableConfig.DISABLE_BINDING_CURSE.getBooleanValue()) {
            return false;
        }

        return EnchantmentHelper.hasBindingCurse(stack);
    }
}

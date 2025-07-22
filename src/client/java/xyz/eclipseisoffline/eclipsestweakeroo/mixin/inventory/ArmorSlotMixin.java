package xyz.eclipseisoffline.eclipsestweakeroo.mixin.inventory;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

@Mixin(targets = "net.minecraft.world.inventory.InventoryMenu$1")
public abstract class ArmorSlotMixin extends Slot {

    public ArmorSlotMixin(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
    public void canAlwaysPlace(ItemStack stack, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_EQUIPMENT_RESTRICTION)) {
            callbackInfoReturnable.setReturnValue(true);
        }
    }

    @WrapOperation(method = "mayPickup", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;hasBindingCurse(Lnet/minecraft/world/item/ItemStack;)Z"))
    public boolean disableBindingCurse(ItemStack stack, Operation<Boolean> original) {
        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_BINDING_CURSE)) {
            return false;
        }

        return original.call(stack);
    }
}

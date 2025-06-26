package xyz.eclipseisoffline.eclipsestweakeroo.mixin.inventory;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;

@Mixin(targets = "net.minecraft.world.inventory.ArmorSlot")
public abstract class ArmorSlotMixin extends Slot {

    public ArmorSlotMixin(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
    public void canAlwaysPlace(ItemStack stack, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (EclipsesDisableConfig.DISABLE_EQUIPMENT_RESTRICTION.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(true);
        }
    }

    @WrapOperation(method = "mayPickup", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;has(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/component/DataComponentType;)Z"))
    public boolean disableBindingCurse(ItemStack stack, DataComponentType<?> componentType, Operation<Boolean> original) {
        if (EclipsesDisableConfig.DISABLE_BINDING_CURSE.getBooleanValue()) {
            return false;
        }

        return original.call(stack, componentType);
    }
}

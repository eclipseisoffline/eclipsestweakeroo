package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin extends RangedWeaponItem {

    public CrossbowItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "getPullProgress", at = @At("HEAD"), cancellable = true)
    private static void isAlwaysCharged(int useTicks, ItemStack stack, LivingEntity user, CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (AdditionalDisableConfig.DISABLE_BOW_DRAW_TIME.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(1.0F);
        }
    }
}

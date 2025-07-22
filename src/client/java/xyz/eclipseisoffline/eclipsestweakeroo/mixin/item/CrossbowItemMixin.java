package xyz.eclipseisoffline.eclipsestweakeroo.mixin.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin extends ProjectileWeaponItem {

    public CrossbowItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "getPowerForTime", at = @At("HEAD"), cancellable = true)
    private static void isAlwaysCharged(int useTime, ItemStack crossbowStack, CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_BOW_DRAW_TIME)) {
            callbackInfoReturnable.setReturnValue(1.0F);
        }
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.mixin.item;

import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

@Mixin(BowItem.class)
public abstract class BowItemMixin extends ProjectileWeaponItem {

    public BowItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "getPowerForTime", at = @At("HEAD"), cancellable = true)
    private static void fullPullProgress(int charge, CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_BOW_DRAW_TIME)) {
            callbackInfoReturnable.setReturnValue(1.0F);
        }
    }
}

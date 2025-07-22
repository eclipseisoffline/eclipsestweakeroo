package xyz.eclipseisoffline.eclipsestweakeroo.mixin.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

@Mixin(ItemCooldowns.class)
public abstract class ItemCooldownsMixin {

    @Inject(method = "addCooldown", at = @At("HEAD"), cancellable = true)
    public void disableItemCooldown(Item item, int ticks, CallbackInfo callbackInfo) {
        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_ITEM_COOLDOWN)) {
            callbackInfo.cancel();
        }
    }
}

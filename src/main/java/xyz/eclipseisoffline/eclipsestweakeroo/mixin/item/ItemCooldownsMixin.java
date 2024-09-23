package xyz.eclipseisoffline.eclipsestweakeroo.mixin.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;

@Mixin(ItemCooldowns.class)
public abstract class ItemCooldownsMixin {

    @Inject(method = "addCooldown", at = @At("HEAD"), cancellable = true)
    public void disableItemCooldown(Item item, int duration, CallbackInfo callbackInfo) {
        if (AdditionalDisableConfig.DISABLE_ITEM_COOLDOWN.getBooleanValue()) {
            callbackInfo.cancel();
        }
    }
}

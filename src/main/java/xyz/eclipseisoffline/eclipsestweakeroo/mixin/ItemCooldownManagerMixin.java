package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;

@Mixin(ItemCooldownManager.class)
public abstract class ItemCooldownManagerMixin {

    @Inject(method = "set", at = @At("HEAD"), cancellable = true)
    public void disableItemCooldown(Item item, int duration, CallbackInfo callbackInfo) {
        if (AdditionalDisableConfig.DISABLE_ITEM_COOLDOWN.getBooleanValue()) {
            callbackInfo.cancel();
        }
    }
}

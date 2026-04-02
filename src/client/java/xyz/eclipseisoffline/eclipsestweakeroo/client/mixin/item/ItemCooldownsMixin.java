package xyz.eclipseisoffline.eclipsestweakeroo.client.mixin.item;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemCooldowns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.client.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.client.util.ToggleManager;

@Mixin(ItemCooldowns.class)
public abstract class ItemCooldownsMixin {

    @Inject(method = "addCooldown(Lnet/minecraft/resources/Identifier;I)V", at = @At("HEAD"), cancellable = true)
    public void disableItemCooldown(Identifier identifier, int ticks, CallbackInfo callbackInfo) {
        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_ITEM_COOLDOWN)) {
            callbackInfo.cancel();
        }
    }
}

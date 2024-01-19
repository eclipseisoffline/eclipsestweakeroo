package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.util.EclipsesTweakerooUtil;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "getDisplayName", at = @At("TAIL"), cancellable = true)
    public void getDisplayName(CallbackInfoReturnable<Text> callbackInfoReturnable) {
        //noinspection ConstantValue
        if (AdditionalFeatureToggle.TWEAK_MOB_NAMES.getBooleanValue()
                && ((Object) this instanceof LivingEntity)) {
            MutableText name = (MutableText) callbackInfoReturnable.getReturnValue();
            EclipsesTweakerooUtil.applyFancyName(
                    (int) ((LivingEntity) (Object) this).getHealth(), name);
            callbackInfoReturnable.setReturnValue(name);
        }
    }

    @Inject(method = "Lnet/minecraft/entity/Entity;pushAwayFrom(Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
    public void pushAwayFrom(Entity entity, CallbackInfo callbackInfo) {
        //noinspection ConstantValue
        if (((Object) this instanceof PlayerEntity) && AdditionalDisableConfig.DISABLE_ENTITY_COLLISIONS.getBooleanValue()) {
            callbackInfo.cancel();
        }
    }
}

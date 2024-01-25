package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.util.EclipsesTweakerooUtil;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Unique
    private static final int SPACE = 2;
    @Unique
    private static final int SPRITE_SIZE = 24;

    @Inject(method = "renderStatusEffectOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;isAmbient()Z"))
    public void drawStatusText(DrawContext context, CallbackInfo callbackInfo,
            @Local StatusEffectInstance statusEffectInstance, @Local(name = "k") int k, @Local(name = "l") LocalIntRef l) {
        if (AdditionalFeatureToggle.TWEAK_STATUS_EFFECT.getBooleanValue()) {
            TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
            // Second row
            if (l.get() > 20) {
                l.set(l.get() + SPACE + renderer.fontHeight);
            }

            Text durationText = EclipsesTweakerooUtil.getDurationTextWithStyle(statusEffectInstance);
            int textWidth = renderer.getWidth(durationText);
            context.drawTextWithShadow(renderer, durationText,
                    k + (SPRITE_SIZE / 2) - (textWidth / 2), l.get() + SPRITE_SIZE + SPACE, -1);
        }
    }
}
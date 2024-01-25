package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
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
    public void drawStatusText(MatrixStack matrices, CallbackInfo callbackInfo,
            @Local StatusEffectInstance statusEffectInstance, @Local(ordinal = 2) int k, @Local(ordinal = 3) LocalIntRef l) {
        if (AdditionalFeatureToggle.TWEAK_STATUS_EFFECT.getBooleanValue()) {
            TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
            // Second row
            if (l.get() > 20) {
                l.set(l.get() + SPACE + renderer.fontHeight);
            }

            Text durationText = EclipsesTweakerooUtil.getDurationTextWithStyle(statusEffectInstance);
            int textWidth = renderer.getWidth(durationText);
            renderer.drawWithShadow(matrices, durationText,
                    k + ((float) SPRITE_SIZE / 2) - ((float) textWidth / 2),
                    l.get() + SPRITE_SIZE + SPACE, -1);
            RenderSystem.setShaderTexture(0, HandledScreen.BACKGROUND_TEXTURE);
        }
    }
}

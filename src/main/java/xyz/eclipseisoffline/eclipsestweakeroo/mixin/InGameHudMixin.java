package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.EclipsesTweakerooUtil;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Unique
    private static final int HOTBAR_WIDTH = 182;
    @Unique
    private static final int HOTBAR_HEIGHT = 22;
    @Unique
    private static final int EXPERIENCE_BAR_HEIGHT = 7;
    @Unique
    private static final int EXPERIENCE_BAR_HEIGHT_UNDERWATER = 13;
    @Unique
    private static final int TEXT_MARGIN = 1;
    @Unique
    private static final int STATUS_EFFECT_SPACE = 2;
    @Unique
    private static final int STATUS_EFFECT_SPRITE_SIZE = 24;
    @Unique
    private static final int SPACE = 2;
    @Unique
    private static final int SPRITE_SIZE = 24;
    @Shadow
    private int scaledWidth;
    @Shadow
    private int scaledHeight;
    @Shadow
    private int ticks;

    @Shadow
    @Nullable
    protected abstract PlayerEntity getCameraPlayer();

    @Shadow
    public abstract TextRenderer getTextRenderer();

    @Inject(method = "renderStatusEffectOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;isAmbient()Z"))
    public void drawStatusText(MatrixStack matrices, CallbackInfo callbackInfo,
            @Local StatusEffectInstance statusEffectInstance, @Local(ordinal = 2) int k, @Local(ordinal = 3) LocalIntRef l) {
        if (AdditionalFeatureToggle.TWEAK_STATUS_EFFECT.getBooleanValue()) {
            TextRenderer renderer = getTextRenderer();
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

    @Inject(method = "renderMountHealth", at = @At("HEAD"), cancellable = true)
    public void cancelMountHealthBarWithNumberHud(MatrixStack matrices, CallbackInfo callbackInfo) {
        if (AdditionalFeatureToggle.TWEAK_NUMBER_HUD.getBooleanValue()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "renderStatusBars", at = @At("HEAD"), cancellable = true)
    public void useNumberHud(MatrixStack matrices, CallbackInfo callbackInfo) {
        if (!AdditionalFeatureToggle.TWEAK_NUMBER_HUD.getBooleanValue()) {
            return;
        }
        callbackInfo.cancel();

        PlayerEntity player = getCameraPlayer();
        if (player == null) {
            return;
        }

        TextRenderer renderer = getTextRenderer();
        Text healthText = getHealthText(player);

        HungerManager hungerManager = player.getHungerManager();
        int hunger = hungerManager.getFoodLevel();
        int saturation = MathHelper.ceil(hungerManager.getSaturationLevel());
        int air = player.getAir();
        int maxAir = player.getMaxAir();

        MutableText airHungerText = Text.empty();
        if (air != maxAir) {
            airHungerText.append(
                    Text.literal(air + "/" + maxAir + " ").formatted(getAirTextColour(player)));
        }

        airHungerText.append(Text.literal(hunger + "/20 (" + saturation + ")")
                .formatted(getHungerTextColour(player)));

        renderer.drawWithShadow(matrices, healthText,
                calculateHudLineX(healthText, false),
                calculateHudLineY(0), 0);
        renderer.drawWithShadow(matrices, airHungerText,
                calculateHudLineX(airHungerText, true),
                calculateHudLineY(0), 0);

        boolean shownArmorText = false;
        Text armorText = EclipsesTweakerooUtil.getArmorText(player);
        if (armorText != null) {
            shownArmorText = true;
            renderer.drawWithShadow(matrices, armorText,
                    calculateHudLineX(armorText, false),
                    calculateHudLineY(1), 0);
        }

        Text attackDamageText = EclipsesTweakerooUtil.getAttackDamageText(player, true);
        renderer.drawWithShadow(matrices, attackDamageText,
                calculateHudLineX(attackDamageText, true),
                calculateHudLineY(1), 0);

        if (AdditionalGenericConfig.TWEAK_NUMBER_HUD_SHOW_DURABILITY_WARNING.getBooleanValue()) {
            StringBuilder durabilityWarnString = new StringBuilder();
            for (int hotbarSlot = 0; hotbarSlot < 8; hotbarSlot++) {
                if (EclipsesTweakerooUtil.shouldWarnDurability(
                        player.getInventory().getStack(hotbarSlot))) {
                    durabilityWarnString.append(hotbarSlot + 1);
                    durabilityWarnString.append("+");
                }
            }
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot == EquipmentSlot.MAINHAND) {
                    continue;
                }
                if (EclipsesTweakerooUtil.shouldWarnDurability(player.getEquippedStack(slot))) {
                    durabilityWarnString.append(slot.toString().substring(0, 1).toUpperCase());
                    durabilityWarnString.append("+");
                }
            }

            if (!durabilityWarnString.isEmpty()) {
                durabilityWarnString.deleteCharAt(durabilityWarnString.length() - 1);
                Text durabilityWarnText = Text.literal(durabilityWarnString.toString())
                        .formatted(getWarnTextColour());
                renderer.drawWithShadow(matrices, durabilityWarnText,
                        calculateHudLineX(durabilityWarnText, true),
                        calculateHudLineY(2), 0);
            }
        }

        if (player.hasVehicle() && player.getVehicle() instanceof LivingEntity vehicle) {
            Text vehicleHealthText = getHealthText(vehicle);
            renderer.drawWithShadow(matrices, vehicleHealthText,
                    calculateHudLineX(vehicleHealthText, false),
                    calculateHudLineY(shownArmorText ? 2 : 1), 0);
        }
    }

    @Unique
    private int calculateHudLineX(Text text, boolean right) {
        if (right) {
            int hotbarRight = scaledWidth / 2 + HOTBAR_WIDTH / 2;
            return hotbarRight - getTextRenderer().getWidth(text);
        }
        // hotbarLeft
        return scaledWidth / 2 - HOTBAR_WIDTH / 2;
    }

    @Unique
    private int calculateHudLineY(int line) {
        int experienceBarHeight = EXPERIENCE_BAR_HEIGHT;

        assert getCameraPlayer() != null;
        if (getCameraPlayer().getAir() < getCameraPlayer().getMaxAir()) {
            experienceBarHeight = EXPERIENCE_BAR_HEIGHT_UNDERWATER;
        }
        int bottomY = scaledHeight - HOTBAR_HEIGHT - experienceBarHeight;
        return bottomY - (getTextRenderer().fontHeight + TEXT_MARGIN) * (line + 1);
    }

    @Unique
    private Text getHealthText(LivingEntity entity) {
        int health = MathHelper.ceil(entity.getHealth());
        int maxHealth = MathHelper.ceil(entity.getMaxHealth());
        int extraHealth = MathHelper.ceil(entity.getAbsorptionAmount());
        MutableText healthText = Text.literal(health + "/" + maxHealth)
                .formatted(getHealthTextColour(entity));
        if (extraHealth > 0) {
            healthText.append(Text.literal("+" + extraHealth).formatted(Formatting.YELLOW));
        }
        return healthText;
    }

    @Unique
    private Formatting getHealthTextColour(LivingEntity entity) {
        if (isWarningTick() && entity instanceof PlayerEntity
                && entity.getHealth() <= AdditionalGenericConfig
                .TWEAK_NUMBER_HUD_HEALTH_WARNING_THRESHOLD.getIntegerValue()) {
            return Formatting.WHITE;
        } else if (entity.hasStatusEffect(StatusEffects.WITHER)) {
            return Formatting.DARK_GRAY;
        } else if (entity.hasStatusEffect(StatusEffects.POISON)) {
            return Formatting.DARK_GREEN;
        } else if (entity.hasStatusEffect(StatusEffects.REGENERATION)) {
            return Formatting.DARK_RED;
        }
        return entity instanceof PlayerEntity ? Formatting.RED : Formatting.GOLD;
    }

    @Unique
    private Formatting getHungerTextColour(PlayerEntity player) {
        if (isWarningTick()
                && player.getHungerManager().getFoodLevel() <= AdditionalGenericConfig
                .TWEAK_NUMBER_HUD_HUNGER_WARNING_THRESHOLD.getIntegerValue()) {
            return Formatting.WHITE;
        } else if (player.hasStatusEffect(StatusEffects.HUNGER)) {
            return Formatting.DARK_GREEN;
        } else if (player.hasStatusEffect(StatusEffects.SATURATION)) {
            return Formatting.AQUA;
        }
        return Formatting.DARK_AQUA;
    }

    @Unique
    private Formatting getAirTextColour(PlayerEntity player) {
        if (player.hasStatusEffect(StatusEffects.WATER_BREATHING)) {
            return Formatting.AQUA;
        } else if (isWarningTick()
                && player.getAir() <= AdditionalGenericConfig
                .TWEAK_NUMBER_HUD_AIR_WARNING_THRESHOLD.getIntegerValue()) {
            return Formatting.WHITE;
        }
        return Formatting.BLUE;
    }

    @Unique
    private Formatting getWarnTextColour() {
        if (isWarningTick()) {
            return Formatting.WHITE;
        }
        return Formatting.GOLD;
    }

    @Unique
    private boolean isWarningTick() {
        return (ticks / 4) % 2 == 0;
    }
}

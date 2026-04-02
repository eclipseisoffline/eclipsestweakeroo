package xyz.eclipseisoffline.eclipsestweakeroo.client.mixin.renderer;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.client.config.EclipsesDisableConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.client.config.EclipsesGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.client.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.client.util.EclipsesTweakerooUtil;
import xyz.eclipseisoffline.eclipsestweakeroo.client.util.ToggleManager;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Unique
    private static final int TEXT_COLOUR = 0xFFFFFFFF;
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

    @Shadow
    private int tickCount;

    @Shadow
    protected abstract @Nullable Player getCameraPlayer();

    @Shadow
    public abstract Font getFont();

    @Inject(method = "extractCameraOverlays", at = @At("HEAD"), cancellable = true)
    public void cancelOverlays(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo callbackInfo) {
        if (ToggleManager.enabled(EclipsesDisableConfig.DISABLE_OVERLAY_RENDER)) {
            callbackInfo.cancel();
        }
    }

    @WrapWithCondition(method = "onDisconnected", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ChatComponent;clearMessages(Z)V"))
    public boolean checkPersistentChatTweak(ChatComponent instance, boolean clearSentMsgHistory) {
        return !ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_PERSISTENT_CHAT);
    }

    @Inject(method = "extractEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;isAmbient()Z"))
    public void drawStatusText(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo callbackInfo,
                               @Local(name = "instance") MobEffectInstance mobEffectInstance, @Local(name = "x") int x, @Local(name = "y") LocalIntRef y) {
        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_STATUS_EFFECT)) {
            // Second row
            if (y.get() > 20) {
                y.set(y.get() + STATUS_EFFECT_SPACE + getFont().lineHeight);
            }

            Component durationText = EclipsesTweakerooUtil.getDurationTextWithStyle(mobEffectInstance);
            int textWidth = getFont().width(durationText);
            graphics.text(getFont(), durationText,
                    x + (STATUS_EFFECT_SPRITE_SIZE / 2) - (textWidth / 2),
                    y.get() + STATUS_EFFECT_SPRITE_SIZE + STATUS_EFFECT_SPACE, TEXT_COLOUR);
        }
    }

    @Inject(method = "extractVehicleHealth", at = @At("HEAD"), cancellable = true)
    public void renderVehicleHealthInCreativeWithNumberHud(GuiGraphicsExtractor graphics, CallbackInfo callbackInfo) {
        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_NUMBER_HUD)) {
            callbackInfo.cancel();
        } else {
            return;
        }

        Player player = getCameraPlayer();
        if (player != null && player.isCreative()) {
            if (getCameraPlayer().getVehicle() instanceof LivingEntity vehicle) {
                drawVehicleHealthText(vehicle, graphics, 0);
            }
        }
    }

    @Inject(method = "extractPlayerHealth", at = @At("HEAD"), cancellable = true)
    public void useNumberHud(GuiGraphicsExtractor graphics, CallbackInfo callbackInfo) {
        if (!ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_NUMBER_HUD)) {
            return;
        }
        callbackInfo.cancel();

        Player player = getCameraPlayer();
        if (player == null) {
            return;
        }

        Component healthText = getHealthText(player);

        FoodData hungerManager = player.getFoodData();
        int hunger = hungerManager.getFoodLevel();
        String saturation = EclipsesTweakerooUtil.roundToOneDecimal(hungerManager.getSaturationLevel());
        int air = player.getAirSupply();
        int maxAir = player.getMaxAirSupply();

        MutableComponent airHungerText = Component.empty();
        if (air != maxAir) {
            airHungerText.append(Component.literal(air + "/" + maxAir + " ").withStyle(getAirTextColour(player)));
        }

        airHungerText.append(Component.literal(hunger + "/20 (" + saturation + ")").withStyle(getHungerTextColour(player)));

        graphics.text(getFont(), healthText,
                calculateHudLineX(graphics, healthText, false),
                calculateHudLineY(graphics, 0), TEXT_COLOUR);
        graphics.text(getFont(), airHungerText,
                calculateHudLineX(graphics, airHungerText, true),
                calculateHudLineY(graphics, 0), TEXT_COLOUR);

        boolean shownArmorText = false;
        Component armorText = EclipsesTweakerooUtil.getArmorText(player);
        if (armorText != null) {
            shownArmorText = true;
            graphics.text(getFont(), armorText,
                    calculateHudLineX(graphics, armorText, false),
                    calculateHudLineY(graphics, 1), TEXT_COLOUR);
        }

        Component attackDamageText = EclipsesTweakerooUtil.getAttackDamageText(player, true);
        graphics.text(getFont(), attackDamageText,
                calculateHudLineX(graphics, attackDamageText, true),
                calculateHudLineY(graphics, 1), TEXT_COLOUR);

        if (EclipsesGenericConfig.TWEAK_NUMBER_HUD_SHOW_DURABILITY_WARNING.getBooleanValue()) {
            StringBuilder durabilityWarnString = new StringBuilder();
            for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
                if (EclipsesTweakerooUtil.shouldWarnDurability(player.getInventory().getItem(hotbarSlot))) {
                    durabilityWarnString.append(hotbarSlot + 1);
                    durabilityWarnString.append("+");
                }
            }
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot == EquipmentSlot.MAINHAND) {
                    continue;
                }
                if (EclipsesTweakerooUtil.shouldWarnDurability(player.getItemBySlot(slot))) {
                    durabilityWarnString.append(slot.getSerializedName().substring(0, 1).toUpperCase());
                    durabilityWarnString.append("+");
                }
            }

            if (!durabilityWarnString.isEmpty()) {
                durabilityWarnString.deleteCharAt(durabilityWarnString.length() - 1);
                Component durabilityWarnText = Component.literal(durabilityWarnString.toString()).withStyle(getDurabilityWarnTextColour());
                graphics.text(getFont(), durabilityWarnText,
                        calculateHudLineX(graphics, durabilityWarnText, true),
                        calculateHudLineY(graphics, 2), TEXT_COLOUR);
            }
        }

        if (player.getVehicle() instanceof LivingEntity vehicle) {
            drawVehicleHealthText(vehicle, graphics, shownArmorText ? 2 : 1);
        }
    }

    @Unique
    private void drawVehicleHealthText(LivingEntity vehicle, GuiGraphicsExtractor graphics, int line) {
        Component vehicleHealthText = getHealthText(vehicle);
        graphics.text(getFont(), vehicleHealthText,
                calculateHudLineX(graphics, vehicleHealthText, false),
                calculateHudLineY(graphics, line), TEXT_COLOUR);
    }

    @Unique
    private int calculateHudLineX(GuiGraphicsExtractor graphics, Component text, boolean right) {
        if (right) {
            int hotbarRight = graphics.guiWidth() / 2 + HOTBAR_WIDTH / 2;
            return hotbarRight - getFont().width(text);
        }
        // hotbarLeft
        return graphics.guiWidth() / 2 - HOTBAR_WIDTH / 2;
    }

    @Unique
    private int calculateHudLineY(GuiGraphicsExtractor graphics, int line) {
        int experienceBarHeight = EXPERIENCE_BAR_HEIGHT;

        Player camera = getCameraPlayer();
        assert camera != null;
        if (camera.getAirSupply() < camera.getMaxAirSupply()) {
            experienceBarHeight = EXPERIENCE_BAR_HEIGHT_UNDERWATER;
        }
        int bottomY = graphics.guiHeight() - HOTBAR_HEIGHT - experienceBarHeight;
        return bottomY - (getFont().lineHeight + TEXT_MARGIN) * (line + 1);
    }

    @Unique
    private Component getHealthText(LivingEntity entity) {
        String health = EclipsesTweakerooUtil.roundToOneDecimal(entity.getHealth());
        String maxHealth = EclipsesTweakerooUtil.roundToOneDecimal(entity.getMaxHealth());
        float extraHealth = entity.getAbsorptionAmount();
        MutableComponent healthText = Component.literal(health + "/" + maxHealth).withStyle(getHealthTextColour(entity));
        if (extraHealth > 0.0F) {
            healthText.append(Component.literal("+" + EclipsesTweakerooUtil.roundToOneDecimal(extraHealth)).withStyle(ChatFormatting.YELLOW));
        }
        return healthText;
    }

    @Unique
    private ChatFormatting getHealthTextColour(LivingEntity entity) {
        if (isWarningTick() && entity instanceof Player
                && entity.getHealth() <= EclipsesGenericConfig.TWEAK_NUMBER_HUD_HEALTH_WARNING_THRESHOLD.getIntegerValue()) {
            return ChatFormatting.WHITE;
        } else if (entity.hasEffect(MobEffects.WITHER)) {
            return ChatFormatting.DARK_GRAY;
        } else if (entity.hasEffect(MobEffects.POISON)) {
            return ChatFormatting.DARK_GREEN;
        } else if (entity.isFreezing()) {
            return ChatFormatting.AQUA;
        } else if (entity.hasEffect(MobEffects.REGENERATION)) {
            return ChatFormatting.DARK_RED;
        }
        return entity instanceof Player ? ChatFormatting.RED : ChatFormatting.GOLD;
    }

    @Unique
    private ChatFormatting getHungerTextColour(Player player) {
        if (isWarningTick() && player.getFoodData().getFoodLevel() <= EclipsesGenericConfig.TWEAK_NUMBER_HUD_HUNGER_WARNING_THRESHOLD.getIntegerValue()) {
            return ChatFormatting.WHITE;
        } else if (player.hasEffect(MobEffects.HUNGER)) {
            return ChatFormatting.DARK_GREEN;
        } else if (player.hasEffect(MobEffects.SATURATION)) {
            return ChatFormatting.AQUA;
        }
        return ChatFormatting.DARK_AQUA;
    }

    @Unique
    private ChatFormatting getAirTextColour(Player player) {
        if (player.hasEffect(MobEffects.WATER_BREATHING)) {
            return ChatFormatting.AQUA;
        } else if (isWarningTick() && player.getAirSupply() <= EclipsesGenericConfig.TWEAK_NUMBER_HUD_AIR_WARNING_THRESHOLD.getIntegerValue()) {
            return ChatFormatting.WHITE;
        }
        return ChatFormatting.BLUE;
    }

    @Unique
    private ChatFormatting getDurabilityWarnTextColour() {
        if (isWarningTick()) {
            return ChatFormatting.WHITE;
        }
        return ChatFormatting.GOLD;
    }

    @Unique
    private boolean isWarningTick() {
        return (tickCount / 4) % 2 == 0;
    }
}

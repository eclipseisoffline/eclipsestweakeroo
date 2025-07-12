package xyz.eclipseisoffline.eclipsestweakeroo.mixin.sounds;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.gui.components.toasts.NowPlayingToastInstance;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

@Mixin(MusicManager.class)
public abstract class MusicManagerMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    public abstract @Nullable String getCurrentMusicTranslationKey();

    @WrapOperation(method = {"startPlaying", "showNowPlayingToastIfNeeded"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/toasts/ToastManager;showNowPlayingToast()V"))
    public void showActionBarInstead(ToastManager instance, Operation<Void> original) {
        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_MUSIC_TOAST)) {
            String translation = getCurrentMusicTranslationKey();
            if (translation != null) {
                Component song = Component.translatable(translation.replace("/", "."));
                if (EclipsesGenericConfig.MUSIC_TOAST_MUSIC.getBooleanValue()) {
                    instance.addToast(new NowPlayingToastInstance(minecraft, song));
                } else {
                    minecraft.gui.setNowPlaying(song);
                }
            }
            return;
        }
        original.call(instance);
    }
}

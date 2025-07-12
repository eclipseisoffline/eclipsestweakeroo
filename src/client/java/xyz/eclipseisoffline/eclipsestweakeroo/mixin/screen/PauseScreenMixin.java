package xyz.eclipseisoffline.eclipsestweakeroo.mixin.screen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.gui.components.toasts.NowPlayingToastInstance;
import xyz.eclipseisoffline.eclipsestweakeroo.util.PlayingJukeboxSongs;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Mixin(PauseScreen.class)
public abstract class PauseScreenMixin extends Screen {

    @Unique
    private final List<NowPlayingToastInstance> musicToasts = new ArrayList<>();
    @Unique
    private Collection<PlayingJukeboxSongs.PlayingJukeboxSong> cachedSongs;
    @Unique
    private String cachedMusic;

    protected PauseScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tickMusicToasts(CallbackInfo callbackInfo) {
        if (shouldShowCustomMusicToasts()) {
            updateMusicToastsIfNecessary();
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/PauseScreen;rendersNowPlayingToast()Z"))
    public void renderCustomMusicToasts(GuiGraphics graphics, int mouseX, int mouseY, float partialTick,
                                        CallbackInfo callbackInfo) {
        for (int i = 0; i < musicToasts.size(); i++) {
            NowPlayingToastInstance toast = musicToasts.get(i);

            graphics.pose().pushMatrix();
            graphics.pose().translate(toast.xPos(width, 1.0F), toast.yPos(i));
            toast.render(graphics, font, 0L);
            graphics.pose().popMatrix();
        }
    }

    @WrapOperation(method = "rendersNowPlayingToast", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;"))
    public Object useOwnMusicToastsWithTweak(OptionInstance instance, Operation original) {
        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_MUSIC_TOAST)) {
            return false;
        }
        return original.call(instance);
    }

    @Unique
    private void updateMusicToastsIfNecessary() {
        if (minecraft != null) {
            PlayingJukeboxSongs.tick(minecraft.level);

            if (EclipsesGenericConfig.MUSIC_TOAST_RECORDS.getBooleanValue() && (cachedSongs == null || PlayingJukeboxSongs.getPlaying().size() != cachedSongs.size())) {
                musicToasts.clear();
                cachedMusic = null;
                cachedSongs = PlayingJukeboxSongs.getPlaying();

                for (PlayingJukeboxSongs.PlayingJukeboxSong song : cachedSongs) {
                    musicToasts.add(new NowPlayingToastInstance(minecraft, song.song().description()));
                }
            }

            String musicPlaying = minecraft.getMusicManager().getCurrentMusicTranslationKey();
            if (EclipsesGenericConfig.MUSIC_TOAST_MUSIC.getBooleanValue() && !Objects.equals(cachedMusic, musicPlaying)) {
                if (cachedMusic != null) {
                    cachedMusic = null;
                    musicToasts.removeFirst();
                } else {
                    cachedMusic = musicPlaying;
                    musicToasts.addFirst(new NowPlayingToastInstance(minecraft, Component.translatable(musicPlaying.replace("/", "."))));
                }
            }

            for (NowPlayingToastInstance toast : musicToasts) {
                toast.update(null, 0L);
            }
        }
    }

    @Unique
    private static boolean shouldShowCustomMusicToasts() {
        return ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_MUSIC_TOAST) && EclipsesGenericConfig.MUSIC_TOAST_PAUSE_MENU.getBooleanValue();
    }
}

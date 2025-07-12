package xyz.eclipseisoffline.eclipsestweakeroo.mixin.level;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.LevelEventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.gui.components.toasts.NowPlayingToastInstance;
import xyz.eclipseisoffline.eclipsestweakeroo.util.PlayingJukeboxSongs;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

@Mixin(LevelEventHandler.class)
public abstract class LevelEventHandlerMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Final
    private Level level;

    @WrapOperation(method = "playJukeboxSong", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;setNowPlaying(Lnet/minecraft/network/chat/Component;)V"))
    public void useMusicToastAndStartSong(Gui instance, Component displayName, Operation<Void> original, @Local(argsOnly = true) BlockPos pos,
                                          @Local(argsOnly = true) Holder<JukeboxSong> song) {
        PlayingJukeboxSongs.playSong(pos, song, level);

        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_MUSIC_TOAST) && EclipsesGenericConfig.MUSIC_TOAST_RECORDS.getBooleanValue()) {
            minecraft.getToastManager().addToast(new NowPlayingToastInstance(minecraft, displayName));
            return;
        }
        original.call(instance, displayName);
    }

    @Inject(method = "stopJukeboxSong", at = @At("HEAD"))
    public void stopSong(BlockPos pos, CallbackInfo callbackInfo) {
        PlayingJukeboxSongs.stopSong(pos);
    }
}

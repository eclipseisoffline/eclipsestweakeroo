package xyz.eclipseisoffline.eclipsestweakeroo.client.mixin.level;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelEventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.JukeboxSong;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.eclipsestweakeroo.client.config.EclipsesGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.client.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.client.gui.components.toasts.NowPlayingToastInstance;
import xyz.eclipseisoffline.eclipsestweakeroo.client.util.PlayingJukeboxSongs;
import xyz.eclipseisoffline.eclipsestweakeroo.client.util.ToggleManager;

@Mixin(LevelEventHandler.class)
public abstract class LevelEventHandlerMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Final
    private ClientLevel level;

    @WrapOperation(method = "playJukeboxSong", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Hud;setNowPlaying(Lnet/minecraft/network/chat/Component;)V"))
    public void useMusicToastAndStartSong(Hud instance, Component string, Operation<Void> original, @Local(argsOnly = true, name = "pos") BlockPos pos, @Local(argsOnly = true, name = "songHolder") Holder<JukeboxSong> song) {
        PlayingJukeboxSongs.playSong(pos, song, level);

        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_MUSIC_TOAST) && EclipsesGenericConfig.MUSIC_TOAST_RECORDS.getBooleanValue()) {
            minecraft.gui.toastManager().addToast(new NowPlayingToastInstance(minecraft, string));
            return;
        }
        original.call(instance, string);
    }

    @Inject(method = "stopJukeboxSong", at = @At("HEAD"))
    public void stopSong(BlockPos pos, CallbackInfo callbackInfo) {
        PlayingJukeboxSongs.stopSong(pos);
    }
}

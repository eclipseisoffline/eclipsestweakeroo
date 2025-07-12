package xyz.eclipseisoffline.eclipsestweakeroo.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PlayingJukeboxSongs {
    private static final Map<BlockPos, PlayingJukeboxSong> playingJukeboxSongs = new HashMap<>();

    public static void playSong(BlockPos pos, Holder<JukeboxSong> song, Level level) {
        playingJukeboxSongs.put(pos, new PlayingJukeboxSong(song.value(), level.getGameTime()));
    }

    public static void tick(Level level) {
        Set<BlockPos> keys = playingJukeboxSongs.keySet();
        for (BlockPos key : keys) {
            if (playingJukeboxSongs.get(key).isExpired(level.getGameTime())) {
                playingJukeboxSongs.remove(key);
            }
        }
    }

    public static void stopSong(BlockPos pos) {
        playingJukeboxSongs.remove(pos);
    }

    public static Collection<PlayingJukeboxSong> getPlaying() {
        return playingJukeboxSongs.values();
    }

    public record PlayingJukeboxSong(JukeboxSong song, long startTick) {

        public boolean isExpired(long tick) {
            return song.hasFinished(tick - startTick);
        }
    }
}

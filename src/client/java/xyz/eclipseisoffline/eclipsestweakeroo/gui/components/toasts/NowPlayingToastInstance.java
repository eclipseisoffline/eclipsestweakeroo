package xyz.eclipseisoffline.eclipsestweakeroo.gui.components.toasts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.ColorLerper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// Same as NowPlayingToast, but allows showing any text component and is instanced, instead of using static variables to store music notes and the current song
public class NowPlayingToastInstance implements Toast {
    // Constants from NowPlayingToast
    private static final Identifier NOW_PLAYING_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("toast/now_playing");
    private static final Identifier MUSIC_NOTES_SPRITE = Identifier.withDefaultNamespace("icon/music_notes");
    private static final int PADDING = 7;
    private static final int MUSIC_NOTES_SIZE = 16;
    private static final int HEIGHT = 30;
    private static final int MUSIC_NOTES_SPACE = 30;
    private static final int VISIBILITY_DURATION = 5000;
    private static final int TEXT_COLOR = DyeColor.LIGHT_GRAY.getTextColor();
    private static final long MUSIC_COLOR_CHANGE_FREQUENCY_MS = 25L;

    private final Component song;
    private final Font font;
    private final double notificationDisplayTimeMultiplier;
    private Visibility wantedVisibility = Visibility.SHOW;

    private int musicNoteColorTick;
    private long lastMusicNoteColorChange;
    private int musicNoteColor = -1;

    public NowPlayingToastInstance(Minecraft minecraft, Component song) {
        this.song = song;
        this.font = minecraft.font;
        notificationDisplayTimeMultiplier = minecraft.options.notificationDisplayTime().get();
    }

    @Override
    public @NotNull Visibility getWantedVisibility() {
        return wantedVisibility;
    }

    @Override
    public void update(@Nullable ToastManager manager, long visibilityTime) {
        wantedVisibility = visibilityTime < VISIBILITY_DURATION * this.notificationDisplayTimeMultiplier ? Toast.Visibility.SHOW : Toast.Visibility.HIDE;
        tickMusicNotes();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, Font font, long fullyVisibleForMs) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, NOW_PLAYING_BACKGROUND_SPRITE, 0, 0, width(), HEIGHT);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, MUSIC_NOTES_SPRITE, PADDING, PADDING, MUSIC_NOTES_SIZE, MUSIC_NOTES_SIZE, musicNoteColor);
        graphics.text(font, song, MUSIC_NOTES_SPACE, 15 - 9 / 2, TEXT_COLOR);
    }

    @Override
    public float xPos(int guiWidth, float visiblePortion) {
        return width() * visiblePortion - width();
    }

    @Override
    public int width() {
        return MUSIC_NOTES_SPACE + font.width(song) + PADDING;
    }

    @Override
    public int height() {
        return HEIGHT;
    }

    private void tickMusicNotes() {
        long millis = System.currentTimeMillis();
        if (millis > lastMusicNoteColorChange + MUSIC_COLOR_CHANGE_FREQUENCY_MS) {
            musicNoteColorTick++;
            lastMusicNoteColorChange = millis;
            musicNoteColor = ColorLerper.getLerpedColor(ColorLerper.Type.MUSIC_NOTE, musicNoteColorTick);
        }
    }
}

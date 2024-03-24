package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import fi.dy.masa.tweakeroo.config.Configs.Disable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.PlayerListOrder;
import xyz.eclipseisoffline.eclipsestweakeroo.util.FancyName;

@Mixin(PlayerListHud.class)
public abstract class PlayerListHudMixin {

    @Unique
    private static final int BOSSBAR_START = 12;
    @Unique
    private static final int BOSSBAR_HEIGHT = 19;

    @Shadow
    private Text header;
    @Shadow
    private Text footer;

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "getPlayerName", at = @At("HEAD"), cancellable = true)
    public void getFancyPlayerName(PlayerListEntry playerListEntry,
            CallbackInfoReturnable<Text> callbackInfoReturnable) {
        if (AdditionalFeatureToggle.TWEAK_PLAYER_LIST.getBooleanValue()
                && AdditionalGenericConfig.TWEAK_PLAYER_LIST_NAMES.getBooleanValue()) {
            assert MinecraftClient.getInstance().world != null;
            PlayerEntity entity = MinecraftClient.getInstance().world.getPlayerByUuid(
                    playerListEntry.getProfile().getId());
            Text playerName = FancyName.applyFancyName(entity, playerListEntry);
            callbackInfoReturnable.setReturnValue(playerName);
        }
    }

    @Inject(method = "collectPlayerEntries", at = @At("HEAD"), cancellable = true)
    public void getCustomOrder(
            CallbackInfoReturnable<List<PlayerListEntry>> callbackInfoReturnable) {
        PlayerListOrder order = (PlayerListOrder) AdditionalGenericConfig.TWEAK_PLAYER_LIST_ORDER.getOptionListValue();
        if (AdditionalFeatureToggle.TWEAK_PLAYER_LIST.getBooleanValue()
                && order.getComparator() != null) {
            assert client.getNetworkHandler() != null;
            callbackInfoReturnable.setReturnValue(
                    client.getNetworkHandler().getListedPlayerListEntries()
                            .stream().sorted(order.getComparator()
                                    .thenComparing(entry -> entry.getProfile().getName(),
                                            String::compareTo)).toList());
        }
    }

    @Redirect(method = "render",
            at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/gui/hud/PlayerListHud;header:Lnet/minecraft/text/Text;"))
    private Text hideHeader(PlayerListHud playerListHud) {
        if (AdditionalFeatureToggle.TWEAK_PLAYER_LIST.getBooleanValue()
                && AdditionalGenericConfig.TWEAK_PLAYER_LIST_HEADER.getBooleanValue()) {
            return null;
        }

        return header;
    }

    @Redirect(method = "render",
            at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/gui/hud/PlayerListHud;footer:Lnet/minecraft/text/Text;"))
    private Text hideFooter(PlayerListHud playerListHud) {
        if (AdditionalFeatureToggle.TWEAK_PLAYER_LIST.getBooleanValue()
                && AdditionalGenericConfig.TWEAK_PLAYER_LIST_FOOTER.getBooleanValue()) {
            return null;
        }
        return footer;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/PlayerListEntry;getGameMode()Lnet/minecraft/world/GameMode;"))
    private GameMode disableSpectatorGameModeFormatting(PlayerListEntry playerListEntry) {
        if (AdditionalFeatureToggle.TWEAK_PLAYER_LIST.getBooleanValue()
                && AdditionalGenericConfig.TWEAK_PLAYER_LIST_NAMES.getBooleanValue()
                && playerListEntry.getGameMode() == GameMode.SPECTATOR) {
            return GameMode.CREATIVE;
        }

        return playerListEntry.getGameMode();
    }

    @Inject(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/PlayerListHud;header:Lnet/minecraft/text/Text;", opcode = Opcodes.GETFIELD, ordinal = 0))
    public void moveBelowBossbar(MatrixStack matrices, int scaledWindowWidth, Scoreboard scoreboard,
            ScoreboardObjective objective, CallbackInfo callbackInfo,
            @Local(ordinal = 9) LocalIntRef renderYStart) {
        if (!AdditionalFeatureToggle.TWEAK_PLAYER_LIST.getBooleanValue()
                || !AdditionalGenericConfig.TWEAK_PLAYER_LIST_BOSSBAR.getBooleanValue()
                || Disable.DISABLE_BOSS_BAR.getBooleanValue()) {
            return;
        }

        BossBarHud bossBarHud = MinecraftClient.getInstance().inGameHud.getBossBarHud();
        Map<UUID, ClientBossBar> bossBars = ((BossBarHudAccessor) bossBarHud).getBossBars();
        if (bossBars.isEmpty()) {
            return;
        }

        int bossBarHeight = BOSSBAR_START + BOSSBAR_HEIGHT * (bossBars.size() - 1);
        int scaledWindowHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
        if (bossBarHeight > scaledWindowHeight / 3) {
            bossBarHeight = scaledWindowHeight / 3;
        }
        renderYStart.set(renderYStart.get() + bossBarHeight);
    }

    @ModifyVariable(method = "render", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private ScoreboardObjective hideScoreboardObjective(ScoreboardObjective scoreboardObjective) {
        if (AdditionalFeatureToggle.TWEAK_PLAYER_LIST.getBooleanValue()
                && AdditionalGenericConfig.TWEAK_PLAYER_LIST_OBJECTIVE.getBooleanValue()) {
            return null;
        }

        return scoreboardObjective;
    }
}

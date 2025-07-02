package xyz.eclipseisoffline.eclipsestweakeroo.mixin.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import fi.dy.masa.tweakeroo.config.Configs.Disable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.EclipsesTweaksConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.config.PlayerListOrder;
import xyz.eclipseisoffline.eclipsestweakeroo.util.FancyName;
import xyz.eclipseisoffline.eclipsestweakeroo.util.ToggleManager;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mixin(PlayerTabOverlay.class)
public abstract class PlayerTabOverlayMixin {

    @Unique
    private static final int BOSSBAR_START = 12;
    @Unique
    private static final int BOSSBAR_HEIGHT = 19;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "getNameForDisplay", at = @At("HEAD"), cancellable = true)
    public void getFancyPlayerName(PlayerInfo playerInfo, CallbackInfoReturnable<Component> callbackInfoReturnable) {
        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_PLAYER_LIST)
                && EclipsesGenericConfig.TWEAK_PLAYER_LIST_NAMES.getBooleanValue()) {
            assert minecraft.level != null;
            Player entity = minecraft.level.getPlayerByUUID(playerInfo.getProfile().getId());
            Component playerName = FancyName.applyFancyName(entity, playerInfo);
            callbackInfoReturnable.setReturnValue(playerName);
        }
    }

    @Inject(method = "getPlayerInfos", at = @At("HEAD"), cancellable = true)
    public void getCustomOrder(CallbackInfoReturnable<List<PlayerInfo>> callbackInfoReturnable) {
        PlayerListOrder order = (PlayerListOrder) EclipsesGenericConfig.TWEAK_PLAYER_LIST_ORDER.getOptionListValue();
        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_PLAYER_LIST)
                && order.getComparator() != null) {
            assert minecraft.getConnection() != null;
            callbackInfoReturnable.setReturnValue(
                    minecraft.getConnection().getListedOnlinePlayers()
                            .stream().sorted(order.getComparator()
                                    .thenComparing(entry -> entry.getProfile().getName(), String::compareTo)).toList());
        }
    }

    @WrapOperation(method = "render",
            at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;header:Lnet/minecraft/network/chat/Component;"))
    private Component hideHeader(PlayerTabOverlay instance, Operation<Component> original) {
        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_PLAYER_LIST) && EclipsesGenericConfig.TWEAK_PLAYER_LIST_HEADER.getBooleanValue()) {
            return null;
        }

        return original.call(instance);
    }

    @WrapOperation(method = "render",
            at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;footer:Lnet/minecraft/network/chat/Component;"))
    private Component hideFooter(PlayerTabOverlay instance, Operation<Component> original) {
        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_PLAYER_LIST) && EclipsesGenericConfig.TWEAK_PLAYER_LIST_FOOTER.getBooleanValue()) {
            return null;
        }

        return original.call(instance);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerInfo;getGameMode()Lnet/minecraft/world/level/GameType;"))
    private GameType disableSpectatorGameModeFormatting(PlayerInfo instance, Operation<GameType> original) {
        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_PLAYER_LIST)
                && EclipsesGenericConfig.TWEAK_PLAYER_LIST_NAMES.getBooleanValue()
                && instance.getGameMode() == GameType.SPECTATOR) {
            return GameType.CREATIVE;
        }

        return original.call(instance);
    }

    @Inject(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;header:Lnet/minecraft/network/chat/Component;", opcode = Opcodes.GETFIELD, ordinal = 0))
    public void moveBelowBossbar(GuiGraphics graphics, int width, Scoreboard scoreboard, Objective objective,
                                 CallbackInfo callbackInfo, @Local(ordinal = 10) LocalIntRef renderYStart) {
        if (!ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_PLAYER_LIST)
                || !EclipsesGenericConfig.TWEAK_PLAYER_LIST_BOSSBAR.getBooleanValue()
                || Disable.DISABLE_BOSS_BAR.getBooleanValue()) {
            return;
        }

        BossHealthOverlay bossBarHud = minecraft.gui.getBossOverlay();
        Map<UUID, LerpingBossEvent> bossBars = ((BossHealthOverlayAccessor) bossBarHud).getEvents();
        if (bossBars.isEmpty()) {
            return;
        }

        int bossBarHeight = BOSSBAR_START + BOSSBAR_HEIGHT * (bossBars.size() - 1);
        if (bossBarHeight > graphics.guiHeight() / 3) {
            bossBarHeight = graphics.guiHeight() / 3;
        }
        renderYStart.set(renderYStart.get() + bossBarHeight);
    }

    @ModifyVariable(method = "render", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private Objective hideScoreboardObjective(Objective scoreboardObjective) {
        if (ToggleManager.enabled(EclipsesTweaksConfig.TWEAK_PLAYER_LIST)
                && EclipsesGenericConfig.TWEAK_PLAYER_LIST_OBJECTIVE.getBooleanValue()) {
            return null;
        }

        return scoreboardObjective;
    }
}

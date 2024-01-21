package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalGenericConfig;
import xyz.eclipseisoffline.eclipsestweakeroo.util.FancyName;

@Mixin(PlayerListHud.class)
public abstract class PlayerListHudMixin {

    @Shadow
    private Text header;
    @Shadow
    private Text footer;

    @Inject(method = "getPlayerName", at = @At("HEAD"), cancellable = true)
    public void getPlayerName(PlayerListEntry playerListEntry,
            CallbackInfoReturnable<Text> callbackInfoReturnable) {
        if (AdditionalFeatureToggle.TWEAK_PLAYER_LIST.getBooleanValue()
                && AdditionalGenericConfig.TWEAK_PLAYER_LIST_NAMES.getBooleanValue()) {
            assert MinecraftClient.getInstance().world != null;
            PlayerEntity entity = MinecraftClient.getInstance().world.getPlayerByUuid(playerListEntry.getProfile().getId());
            Text playerName = FancyName.applyFancyName(entity, playerListEntry);
            callbackInfoReturnable.setReturnValue(playerName);
        }
    }

    @Redirect(method = "render",
            at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/gui/hud/PlayerListHud;header:Lnet/minecraft/text/Text;"))
    private Text getHeader(PlayerListHud playerListHud) {
        if (AdditionalFeatureToggle.TWEAK_PLAYER_LIST.getBooleanValue()
                && AdditionalGenericConfig.TWEAK_PLAYER_LIST_HEADER.getBooleanValue()) {
            return null;
        }

        return header;
    }

    @Redirect(method = "render",
            at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/gui/hud/PlayerListHud;footer:Lnet/minecraft/text/Text;"))
    private Text getFooter(PlayerListHud playerListHud) {
        if (AdditionalFeatureToggle.TWEAK_PLAYER_LIST.getBooleanValue()
                && AdditionalGenericConfig.TWEAK_PLAYER_LIST_FOOTER.getBooleanValue()) {
            return null;
        }
        return footer;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/PlayerListEntry;getGameMode()Lnet/minecraft/world/GameMode;"))
    private GameMode getGameMode(PlayerListEntry playerListEntry) {
        if (AdditionalFeatureToggle.TWEAK_PLAYER_LIST.getBooleanValue()
                && AdditionalGenericConfig.TWEAK_PLAYER_LIST_NAMES.getBooleanValue()
                && playerListEntry.getGameMode() == GameMode.SPECTATOR) {
            return GameMode.CREATIVE;
        }

        return playerListEntry.getGameMode();
    }

    @ModifyVariable(method = "render", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private ScoreboardObjective modifyScoreboardObjective(ScoreboardObjective scoreboardObjective) {
        if (AdditionalFeatureToggle.TWEAK_PLAYER_LIST.getBooleanValue()
                && AdditionalGenericConfig.TWEAK_PLAYER_LIST_OBJECTIVE.getBooleanValue()) {
            return null;
        }

        return scoreboardObjective;
    }
}

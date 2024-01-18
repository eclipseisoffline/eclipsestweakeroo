package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import java.util.Map;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalGenericConfig;

@Mixin(PlayerListHud.class)
public abstract class PlayerListHudMixin {
    @Unique
    private static final Map<GameMode, MutableText> GAMEMODE_TEXT = Map.of(
            GameMode.SURVIVAL, MutableText.of(new LiteralTextContent("S")).setStyle(Style.EMPTY.withColor(
                    Formatting.RED)),
            GameMode.CREATIVE, MutableText.of(new LiteralTextContent("C")).setStyle(Style.EMPTY.withColor(
                    Formatting.GREEN)),
            GameMode.ADVENTURE, MutableText.of(new LiteralTextContent("A")).setStyle(Style.EMPTY.withColor(
                    Formatting.YELLOW)),
            GameMode.SPECTATOR, MutableText.of(new LiteralTextContent("SP")).setStyle(Style.EMPTY.withColor(
                    Formatting.BLUE))
    );

    @Shadow
    private Text header;
    @Shadow
    private Text footer;

    @Inject(method = "getPlayerName", at = @At("HEAD"), cancellable = true)
    public void getPlayerName(PlayerListEntry playerListEntry, CallbackInfoReturnable<Text> callbackInfoReturnable) {
        if (AdditionalFeatureToggle.TWEAK_PLAYER_LIST.getBooleanValue() && AdditionalGenericConfig.TWEAK_PLAYER_LIST_NAMES.getBooleanValue()) {
            MutableText playerName = Team.decorateName(playerListEntry.getScoreboardTeam(), Text.literal(playerListEntry.getProfile().getName()));
            playerName.append(Text.of(" - "));
            playerName.append(GAMEMODE_TEXT.get(playerListEntry.getGameMode()));
            playerName.append(Text.of(" - "));
            playerName.append(MutableText.of(new LiteralTextContent(playerListEntry.getLatency() + "ms"))
                    .setStyle(getPingStyle(playerListEntry.getLatency())));
            callbackInfoReturnable.setReturnValue(playerName);
        }
    }

    @Unique
    private static Style getPingStyle(int ping) {
        if (ping <= 0) {
            return Style.EMPTY.withColor(Formatting.DARK_GRAY);
        } else if (ping <= 150) {
            return Style.EMPTY.withColor(Formatting.GREEN);
        } else if (ping <= 300) {
            return Style.EMPTY.withColor(Formatting.YELLOW);
        } else if (ping <= 600) {
            return Style.EMPTY.withColor(Formatting.GOLD);
        } else if (ping <= 1000) {
            return Style.EMPTY.withColor(Formatting.RED);
        }

        return Style.EMPTY.withColor(Formatting.DARK_RED);
    }

    @Redirect(method = "render",
            at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/gui/hud/PlayerListHud;header:Lnet/minecraft/text/Text;"))
    private Text getHeader(PlayerListHud playerListHud) {
        if (AdditionalFeatureToggle.TWEAK_PLAYER_LIST.getBooleanValue() && AdditionalGenericConfig.TWEAK_PLAYER_LIST_HEADER.getBooleanValue()) {
            return null;
        }

        return header;
    }

    @Redirect(method = "render",
            at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/gui/hud/PlayerListHud;footer:Lnet/minecraft/text/Text;"))
    private Text getFooter(PlayerListHud playerListHud) {
        if (AdditionalFeatureToggle.TWEAK_PLAYER_LIST.getBooleanValue() && AdditionalGenericConfig.TWEAK_PLAYER_LIST_FOOTER.getBooleanValue()) {
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
}

package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;

@Mixin(PlayerListHud.class)
public abstract class PlayerListHudMixin {
    @Shadow
    private Text header;
    @Shadow
    private Text footer;

    @Inject(method = "getPlayerName", at = @At("HEAD"), cancellable = true)
    public void getPlayerName(PlayerListEntry playerListEntry, CallbackInfoReturnable<Text> callbackInfoReturnable) {
        if (AdditionalDisableConfig.DISABLE_DISPLAY_NAME.getBooleanValue()) {
            callbackInfoReturnable.setReturnValue(applyGameModeFormatting(playerListEntry,
                    Team.decorateName(playerListEntry.getScoreboardTeam(),
                            Text.literal(playerListEntry.getProfile().getName()))));
        }
    }

    @Shadow protected abstract Text applyGameModeFormatting(PlayerListEntry entry, MutableText name);

    @Redirect(method = "render",
            at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/gui/hud/PlayerListHud;header:Lnet/minecraft/text/Text;"))
    private Text getHeader(PlayerListHud playerListHud) {
        if (AdditionalDisableConfig.DISABLE_TAB_HEADER.getBooleanValue()) {
            return null;
        }

        return header;
    }

    @Redirect(method = "render",
            at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/gui/hud/PlayerListHud;footer:Lnet/minecraft/text/Text;"))
    private Text getFooter(PlayerListHud playerListHud) {
        if (AdditionalDisableConfig.DISABLE_TAB_FOOTER.getBooleanValue()) {
            return null;
        }
        return footer;
    }
}

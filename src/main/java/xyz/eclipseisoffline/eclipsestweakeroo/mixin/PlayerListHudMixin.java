package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalDisableConfig;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {
    @Shadow
    private Text header;
    @Shadow
    private Text footer;

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

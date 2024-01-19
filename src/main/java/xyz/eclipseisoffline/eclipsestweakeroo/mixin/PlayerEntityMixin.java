package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFeatureToggle;
import xyz.eclipseisoffline.eclipsestweakeroo.util.EclipsesTweakerooUtil;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "getDisplayName", at = @At("TAIL"), cancellable = true)
    public void getDisplayName(CallbackInfoReturnable<Text> callbackInfoReturnable) {
        if (AdditionalFeatureToggle.TWEAK_PLAYER_NAME.getBooleanValue()) {
            if (MinecraftClient.getInstance().getNetworkHandler() == null) {
                return;
            }

            PlayerListEntry playerListEntry = MinecraftClient.getInstance().getNetworkHandler().getPlayerListEntry(((PlayerEntity) (Object) this).getUuid());
            MutableText playerName = (MutableText) callbackInfoReturnable.getReturnValue();
            EclipsesTweakerooUtil.applyFancyName(playerListEntry,
                    (int) ((PlayerEntity) (Object) this).getHealth(), playerName);
            callbackInfoReturnable.setReturnValue(playerName);
        }
    }
}

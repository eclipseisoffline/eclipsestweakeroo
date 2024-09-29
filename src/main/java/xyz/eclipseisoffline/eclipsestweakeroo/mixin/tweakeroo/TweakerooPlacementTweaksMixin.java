package xyz.eclipseisoffline.eclipsestweakeroo.mixin.tweakeroo;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import fi.dy.masa.malilib.util.PositionUtils;
import fi.dy.masa.tweakeroo.tweaks.PlacementTweaks;
import java.util.List;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFixesConfig;

@Mixin(PlacementTweaks.class)
public abstract class TweakerooPlacementTweaksMixin {

    @Unique
    private static final List<Item> ITEMS_TO_PATCH = List.of(Items.PISTON, Items.STICKY_PISTON, Items.DISPENSER, Items.DROPPER, Items.CRAFTER);

    @Inject(method = "tryPlaceBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;", ordinal = 0))
    private static void fixPistonFlexiblePlacement(MultiPlayerGameMode controller, LocalPlayer player, ClientLevel world, BlockPos posIn,
                                                   Direction sideIn, Direction sideRotatedIn, float playerYaw, Vec3 hitVec, InteractionHand hand,
                                                   PositionUtils.HitPart hitPart, boolean isFirstClick,
                                                   CallbackInfoReturnable<InteractionResult> callbackInfoReturnable,
                                                   @Local(ordinal = 1, argsOnly = true) LocalRef<Direction> sideRotatedInLocal,
                                                   @Local(ordinal = 2) boolean flexible,
                                                   @Local(ordinal = 7) boolean rotation) {
        if (!AdditionalFixesConfig.PISTON_FLEXIBLE_PLACEMENT_FIX.getBooleanValue()) {
            return;
        }
        if (flexible && rotation
                && sideIn == sideRotatedInLocal.get().getOpposite()
                && ITEMS_TO_PATCH.contains(player.getItemInHand(hand).getItem())) {
            sideRotatedInLocal.set(sideRotatedInLocal.get().getOpposite());
        }
    }
}

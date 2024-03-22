package xyz.eclipseisoffline.eclipsestweakeroo.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import fi.dy.masa.malilib.util.PositionUtils.HitPart;
import fi.dy.masa.tweakeroo.tweaks.PlacementTweaks;
import java.util.List;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.eclipsestweakeroo.config.AdditionalFixesConfig;

@Mixin(PlacementTweaks.class)
public class TweakerooPlacementTweaksMixin {

    @Unique
    private static final List<Item> ITEMS_TO_PATCH = List.of(Items.PISTON, Items.STICKY_PISTON,
            Items.DISPENSER, Items.DROPPER);

    @Inject(method = "tryPlaceBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;", ordinal = 0), remap = false)
    private static void fixPistonFlexiblePlacement(ClientPlayerInteractionManager controller,
            ClientPlayerEntity player, ClientWorld world, BlockPos posIn, Direction sideIn,
            Direction sideRotatedIn, float playerYaw, Vec3d hitVec, Hand hand, HitPart hitPart,
            boolean isFirstClick, CallbackInfoReturnable<ActionResult> callbackInfoReturnable,
            @Local(ordinal = 1, argsOnly = true) LocalRef<Direction> sideRotatedInLocal,
            @Local(ordinal = 2) boolean flexible,
            @Local(ordinal = 7) boolean rotation) {
        if (!AdditionalFixesConfig.PISTON_FLEXIBLE_PLACEMENT_FIX.getBooleanValue()) {
            return;
        }
        if (flexible && rotation
                && sideIn == sideRotatedInLocal.get().getOpposite()
                && ITEMS_TO_PATCH.contains(player.getStackInHand(hand).getItem())) {
            sideRotatedInLocal.set(sideRotatedInLocal.get().getOpposite());
        }
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.client.mixin.tweakeroo.mod;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import fi.dy.masa.malilib.util.position.PositionUtils;
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
import xyz.eclipseisoffline.eclipsestweakeroo.client.config.EclipsesFixesConfig;

@Mixin(PlacementTweaks.class)
public abstract class TweakerooPlacementTweaksMixin {

    @Unique
    private static final ScopedValue<BlockPos> ORIGINAL_BLOCK_PLACE_POS = ScopedValue.newInstance();
    @Unique
    private static final ScopedValue<Direction> ORIGINAL_BLOCK_PLACE_SIDE = ScopedValue.newInstance();

    @Unique
    private static final List<Item> ITEMS_TO_PATCH = List.of(Items.PISTON, Items.STICKY_PISTON, Items.DISPENSER, Items.DROPPER, Items.CRAFTER);

    @Inject(method = "tryPlaceBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;", ordinal = 0))
    private static void fixPistonFlexiblePlacement(MultiPlayerGameMode controller, LocalPlayer player, ClientLevel world, BlockPos posIn, Direction sideIn, Direction sideRotatedIn, float playerYaw, Vec3 hitVec, InteractionHand hand,
                                                   PositionUtils.HitPart hitPart,
                                                   boolean isFirstClick, CallbackInfoReturnable<InteractionResult> cir,
                                                   @Local(ordinal = 1, argsOnly = true) LocalRef<Direction> sideRotatedInLocal,
                                                   @Local(name = "flexible") boolean flexible,
                                                   @Local(name = "rotation") boolean rotation) {
        if (!EclipsesFixesConfig.PISTON_FLEXIBLE_PLACEMENT_FIX.getBooleanValue()) {
            return;
        }
        if (flexible && rotation
                && sideIn == sideRotatedInLocal.get().getOpposite()
                && ITEMS_TO_PATCH.contains(player.getItemInHand(hand).getItem())) {
            sideRotatedInLocal.set(sideRotatedInLocal.get().getOpposite());
        }
    }

    @WrapMethod(method = "tryPlaceBlock")
    private static InteractionResult setScopedValues(MultiPlayerGameMode controller, LocalPlayer player, ClientLevel world, BlockPos posIn, Direction sideIn, Direction sideRotatedIn,
                                                     float playerYaw, Vec3 hitVec, InteractionHand hand, PositionUtils.HitPart hitPart, boolean isFirstClick, Operation<InteractionResult> original) {
        if (EclipsesFixesConfig.FAST_PLACEMENT_ROTATION_FIX.getBooleanValue()) {
            return ScopedValue.where(ORIGINAL_BLOCK_PLACE_POS, posIn)
                    .where(ORIGINAL_BLOCK_PLACE_SIDE, sideIn)
                    .call(() -> original.call(controller, player, world, posIn, sideIn, sideRotatedIn, playerYaw, hitVec, hand, hitPart, isFirstClick));
        }
        return original.call(controller, player, world, posIn, sideIn, sideRotatedIn, playerYaw, hitVec, hand, hitPart, isFirstClick);
    }

    @WrapOperation(method = "processRightClickBlockWrapper", at = @At(value = "INVOKE", target = "Lfi/dy/masa/tweakeroo/tweaks/PlacementTweaks;isPositionAllowedByPlacementRestriction(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z"))
    private static boolean useOriginalScopedValues(BlockPos pos, Direction side, Operation<Boolean> original) {
        if (EclipsesFixesConfig.FAST_PLACEMENT_ROTATION_FIX.getBooleanValue()) {
            return original.call(ORIGINAL_BLOCK_PLACE_POS.get(), ORIGINAL_BLOCK_PLACE_SIDE.get());
        }
        return original.call(pos, side);
    }
}

package xyz.eclipseisoffline.eclipsestweakeroo.client.mixin.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import xyz.eclipseisoffline.eclipsestweakeroo.client.inventory.MerchantMenuQuickSell;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Mixin(MerchantMenu.class)
public abstract class MerchantMenuMixin extends AbstractContainerMenu implements MerchantMenuQuickSell {
    @Shadow
    public abstract ItemStack quickMoveStack(Player player, int slotIndex);

    @Shadow
    public abstract MerchantOffers getOffers();

    @Unique
    private boolean waitingForTrade = false;
    @Unique
    private final List<MerchantOffer> tradeAllPreviousOffers = new ArrayList<>();
    @Unique
    private Predicate<MerchantOffer> tradeAllPredicate = _ -> true;

    protected MerchantMenuMixin(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    @Unique
    public void eclipsestweakeroo$tradeAll(Predicate<MerchantOffer> predicate) {
        waitingForTrade = false;
        tradeAllPreviousOffers.clear();
        tradeAllPredicate = predicate;
        attemptToBuyTradeAndContinueTradeAll();
    }

    @Unique
    private void attemptToBuyTradeAndContinueTradeAll() {
        LocalPlayer player = Minecraft.getInstance().player;
        MultiPlayerGameMode gameMode = Minecraft.getInstance().gameMode;
        if (player != null && gameMode != null) {
            gameMode.handleContainerInput(containerId, 2, 0, ContainerInput.QUICK_MOVE, player);
            // Try dropping the trade if quick moving into inventory didn't work
            gameMode.handleContainerInput(containerId, 2, 0, ContainerInput.THROW, player);

            // Because the server is "slow" with updating stock (and desyncs on singleplayer sometimes), we keep a list of previously used offers,
            // and go through offers twice: the first pass skips previously used ones and the second goes through all them.
            // If the second pass fails, we end the trade all: all offers are probably out of stock now (or not enough money)
            int offers = getOffers().size();
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < offers; j++) {
                    MerchantOffer offer = getOffers().get(j);
                    if (offer.isOutOfStock() || !tradeAllPredicate.test(offer)
                            || hasSimilarOffer(tradeAllPreviousOffers, offer)
                            || !hasEnoughMoneyToAffordTrade(player, offer.getCostA(), offer.getCostB())) {
                        continue;
                    }
                    player.connection.send(new ServerboundSelectTradePacket(j));
                    waitingForTrade = true;
                    tradeAllPreviousOffers.add(offer);
                    return;
                }
                tradeAllPreviousOffers.clear();
            }
        }
    }

    @Unique
    private static boolean hasEnoughMoneyToAffordTrade(LocalPlayer player, ItemStack costA, ItemStack costB) {
        return isSomewhereInCombinedInventory(player.getInventory(), costA) && isSomewhereInCombinedInventory(player.getInventory(), costB);
    }

    @Unique
    private static boolean isSomewhereInCombinedInventory(Iterable<ItemStack> inventory, ItemStack stack) {
        if (stack.isEmpty()) {
            return true;
        }

        int found = 0;
        int demanded = stack.count();
        for (ItemStack stackInInventory : inventory) {
            if (ItemStack.isSameItemSameComponents(stackInInventory, stack)) {
                found += stackInInventory.count();
                if (found >= demanded) {
                    return true;
                }
            }
        }
        return false;
    }

    @Unique
    private static boolean hasSimilarOffer(List<MerchantOffer> offers, MerchantOffer offer) {
        for (MerchantOffer anOffer : offers) {
            if (isSimilarOffer(anOffer, offer)) {
                return true;
            }
        }
        return false;
    }

    @Unique
    private static boolean isSimilarOffer(MerchantOffer a, MerchantOffer b) {
        return ItemStack.isSameItemSameComponents(a.getBaseCostA(), b.getBaseCostA())
                && ItemStack.isSameItemSameComponents(a.getCostB(), b.getCostB())
                && ItemStack.isSameItemSameComponents(a.getResult(), b.getResult());
    }

    @Override
    public void setItem(int slot, int stateId, ItemStack itemStack) {
        int oldStateId = getStateId();
        super.setItem(slot, stateId, itemStack);
        if (waitingForTrade && stateId > oldStateId) {
            waitingForTrade = false;
            attemptToBuyTradeAndContinueTradeAll();
        }
    }
}

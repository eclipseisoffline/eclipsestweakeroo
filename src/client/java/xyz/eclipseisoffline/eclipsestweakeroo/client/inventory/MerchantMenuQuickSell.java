package xyz.eclipseisoffline.eclipsestweakeroo.client.inventory;

import net.minecraft.world.item.trading.MerchantOffer;

import java.util.function.Predicate;

public interface MerchantMenuQuickSell {

    void eclipsestweakeroo$tradeAll(Predicate<MerchantOffer> predicate);
}

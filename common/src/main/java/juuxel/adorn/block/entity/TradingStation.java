package juuxel.adorn.block.entity;

import juuxel.adorn.trading.Trade;
import juuxel.adorn.util.InventoryComponent;
import net.minecraft.network.chat.Component;

public interface TradingStation {
    Component getOwnerName();
    Trade getTrade();
    InventoryComponent getStorage();

    static TradingStation createEmpty() {
        return new TradingStation() {
            @Override
            public Component getOwnerName() {
                return Component.empty();
            }

            @Override
            public Trade getTrade() {
                return Trade.empty();
            }

            @Override
            public InventoryComponent getStorage() {
                return new InventoryComponent(TradingStationBlockEntity.STORAGE_SIZE);
            }
        };
    }
}

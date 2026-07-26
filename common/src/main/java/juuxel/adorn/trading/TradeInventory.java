package juuxel.adorn.trading;

import juuxel.adorn.util.InventoryComponent;

public final class TradeInventory extends InventoryComponent {
    private final Trade trade;

    public TradeInventory(Trade trade) {
        super(2);
        this.trade = trade;

        setItem(0, trade.getSelling());
        setItem(1, trade.getPrice());

        addListener(() -> {
            trade.setSelling(getItem(0));
            trade.setPrice(getItem(1));
            trade.callListeners();
        });
    }

    public Trade getTrade() {
        return trade;
    }
}

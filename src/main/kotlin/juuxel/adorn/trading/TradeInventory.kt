package juuxel.adorn.trading

import juuxel.adorn.util.InventoryComponent

class TradeInventory(val trade: Trade) : InventoryComponent(2) {
    init {
        setStack(0, trade.selling)
        setStack(1, trade.price)

        addListener {
            trade.selling = getStack(0)
            trade.price = getStack(1)
            trade.callListeners()
        }
    }
}

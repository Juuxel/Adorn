package juuxel.adorn.trading

import juuxel.adorn.util.InventoryComponent

class TradeInventory(val trade: Trade) : InventoryComponent(2) {
    init {
        setInvStack(0, trade.selling)
        setInvStack(1, trade.price)

        addListener {
            trade.selling = getInvStack(0)
            trade.price = getInvStack(1)
            trade.callListeners()
        }
    }
}

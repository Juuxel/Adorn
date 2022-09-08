package juuxel.adorn.block.entity

import juuxel.adorn.trading.Trade
import juuxel.adorn.util.InventoryComponent
import net.minecraft.text.Text

interface TradingStation {
    val ownerName: Text
    val trade: Trade
    val storage: InventoryComponent

    companion object {
        fun createEmpty(): TradingStation = object : TradingStation {
            override val ownerName: Text = Text.empty()
            override val trade = Trade.empty()
            override val storage = InventoryComponent(12)
        }
    }
}

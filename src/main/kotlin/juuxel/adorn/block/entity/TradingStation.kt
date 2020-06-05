package juuxel.adorn.block.entity

import juuxel.adorn.trading.Trade
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack

interface TradingStation {
    val trade: Trade
    val storage: Inventory

    companion object {
        fun createEmpty(): TradingStation = object : TradingStation {
            override val trade = Trade(ItemStack.EMPTY, ItemStack.EMPTY)
            override val storage = SimpleInventory(12)
        }
    }
}

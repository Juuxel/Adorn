package juuxel.adorn.block.entity

import juuxel.adorn.trading.Trade
import juuxel.adorn.util.InventoryComponent
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Util
import java.util.UUID

interface TradingStation {
    val owner: UUID?
    val ownerName: Text
    val trade: Trade
    val storage: InventoryComponent

    fun isStorageStocked(): Boolean
    fun isOwner(player: PlayerEntity): Boolean
    fun setOwner(player: PlayerEntity)

    companion object {
        fun createEmpty(): TradingStation = object : TradingStation {
            override val owner: UUID = Util.NIL_UUID
            override val ownerName: Text = LiteralText("")
            override val trade = Trade(ItemStack.EMPTY, ItemStack.EMPTY)
            override val storage = InventoryComponent(12)

            override fun isStorageStocked() = false
            override fun isOwner(player: PlayerEntity) = false
            override fun setOwner(player: PlayerEntity) {}
        }
    }
}

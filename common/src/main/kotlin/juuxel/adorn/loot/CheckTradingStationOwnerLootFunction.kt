package juuxel.adorn.loot

import com.mojang.serialization.Codec
import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.block.entity.TradingStationBlockEntity
import juuxel.adorn.trading.Trade
import juuxel.adorn.util.InventoryComponent
import juuxel.adorn.util.getCompoundOrNull
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.loot.context.LootContext
import net.minecraft.loot.function.LootFunction
import net.minecraft.loot.function.LootFunctionType
import net.minecraft.nbt.NbtCompound

object CheckTradingStationOwnerLootFunction : LootFunction {
    val CODEC: Codec<CheckTradingStationOwnerLootFunction> = Codec.unit(this)

    override fun apply(stack: ItemStack, context: LootContext): ItemStack {
        if (stack.isOf(AdornBlocks.TRADING_STATION.asItem())) {
            stack.getSubNbt(BlockItem.BLOCK_ENTITY_TAG_KEY)?.let { tag ->
                if (!hasTrade(tag) && !hasStorage(tag)) {
                    clearOwner(tag)
                }
            }
        }

        return stack
    }

    private fun hasTrade(nbt: NbtCompound): Boolean {
        val tradeNbt = nbt.getCompoundOrNull(TradingStationBlockEntity.NBT_TRADE) ?: return false
        val trade = Trade.fromNbt(tradeNbt)
        return !trade.isFullyEmpty()
    }

    private fun hasStorage(nbt: NbtCompound): Boolean {
        val storageNbt = nbt.getCompoundOrNull(TradingStationBlockEntity.NBT_STORAGE) ?: return false
        val inventory = InventoryComponent(TradingStationBlockEntity.STORAGE_SIZE)
        inventory.readNbt(storageNbt)
        return !inventory.isEmpty
    }

    private fun clearOwner(nbt: NbtCompound) {
        nbt.remove(TradingStationBlockEntity.NBT_TRADING_OWNER)
        nbt.remove(TradingStationBlockEntity.NBT_TRADING_OWNER_NAME)
    }

    override fun getType(): LootFunctionType =
        AdornLootFunctionTypes.CHECK_TRADING_STATION_OWNER
}

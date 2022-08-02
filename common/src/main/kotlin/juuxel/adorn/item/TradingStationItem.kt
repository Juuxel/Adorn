package juuxel.adorn.item

import juuxel.adorn.block.entity.TradingStationBlockEntity
import juuxel.adorn.trading.Trade
import juuxel.adorn.util.getCompoundOrNull
import net.minecraft.block.Block
import net.minecraft.client.item.TooltipData
import net.minecraft.item.ItemStack
import java.util.Optional

class TradingStationItem(block: Block, settings: Settings) : BaseBlockItem(block, settings) {
    // Don't allow putting trading stations inside shulker boxes or other trading stations.
    override fun canBeNested(): Boolean = false

    override fun getTooltipData(stack: ItemStack): Optional<TooltipData> {
        val data = stack.getSubNbt(BLOCK_ENTITY_TAG_KEY)?.let { nbt ->
            nbt.getCompoundOrNull(TradingStationBlockEntity.NBT_TRADE)?.let { tradeNbt ->
                val trade = Trade(ItemStack.EMPTY, ItemStack.EMPTY)
                trade.readNbt(tradeNbt)
                trade
            }
        }
        return Optional.ofNullable(data)
    }
}

package juuxel.adorn.item

import net.minecraft.block.Block

class TradingStationItem(block: Block, settings: Settings) : BaseBlockItem(block, settings) {
    // Don't allow putting trading stations inside shulker boxes or other trading stations.
    override fun canBeNested(): Boolean = false
}

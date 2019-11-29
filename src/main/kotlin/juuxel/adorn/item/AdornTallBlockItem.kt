package juuxel.adorn.item

import net.minecraft.block.Block
import net.minecraft.item.ItemGroup
import net.minecraft.item.TallBlockItem

open class AdornTallBlockItem(block: Block, settings: Settings) : TallBlockItem(block, settings) {
    override fun isIn(group: ItemGroup?) = super.isIn(group) || group === AdornItems.GROUP
}

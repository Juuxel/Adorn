package juuxel.adorn.item

import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup

open class BaseBlockItem(block: Block, settings: Item.Settings) : BlockItem(block, settings) {
    override fun isIn(group: ItemGroup?) = super.isIn(group) || group === AdornItems.GROUP
}

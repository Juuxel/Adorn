package juuxel.adorn.item

import juuxel.adorn.lib.AdornItemTags
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack

open class BaseBlockItem(block: Block, settings: Settings) : BlockItem(block, settings) {
    override fun isIn(group: ItemGroup?) = AdornItems.isIn(group, this)

    override fun getBurnTime(stack: ItemStack): Int {
        for ((tag, burnTime) in AdornItemTags.BURN_TIMES) {
            if (tag.contains(this)) {
                return burnTime
            }
        }

        return -1
    }
}

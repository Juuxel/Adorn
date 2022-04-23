package juuxel.adorn.item

import juuxel.adorn.lib.FuelData
import juuxel.adorn.util.TagWithState
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack

open class BaseBlockItem(block: Block, settings: Settings) : BlockItem(block, settings) {
    override fun isIn(group: ItemGroup?) = AdornItems.isIn(group, this)

    override fun getBurnTime(stack: ItemStack): Int {
        for (fuelData in FuelData.ALL) {
            val tag = fuelData.tag

            // Don't read the tag if it's not ready to be read.
            // See https://github.com/Juuxel/Adorn/issues/137
            if (tag is TagWithState && !tag.adorn_isReady()) {
                if ((fuelData.blockClass ?: continue).isInstance(block)) {
                    return fuelData.burnTime
                }
            } else if (tag.contains(this)) {
                return fuelData.burnTime
            }
        }

        return -1
    }
}

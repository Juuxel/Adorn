package juuxel.adorn.item

import net.minecraft.block.Block
import net.minecraft.block.CarpetBlock
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.ActionResult
import net.minecraft.util.math.Direction

class TableBlockItem(block: Block) : BlockItem(block, Settings().group(ItemGroup.DECORATIONS)) {
    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val world = context.world
        val pos = context.blockPos
        if (context.side == Direction.UP && world.getBlockState(pos).block is CarpetBlock) {
            place(CarpetedTopPlacementContext(context))
            return ActionResult.SUCCESS
        }

        return super.useOnBlock(context)
    }

    override fun isIn(group: ItemGroup?) = AdornItems.isIn(group, this)
}

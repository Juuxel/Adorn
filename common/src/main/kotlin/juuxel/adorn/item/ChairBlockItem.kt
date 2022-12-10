package juuxel.adorn.item

import net.minecraft.block.Block
import net.minecraft.block.CarpetBlock
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.ActionResult
import net.minecraft.util.math.Direction

class ChairBlockItem(block: Block) : AdornTallBlockItem(block, Settings()) {
    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val world = context.world
        val pos = context.blockPos
        if (context.side == Direction.UP && world.getBlockState(pos).block is CarpetBlock) {
            place(CarpetedTopPlacementContext(context))
            return ActionResult.SUCCESS
        }

        return super.useOnBlock(context)
    }
}

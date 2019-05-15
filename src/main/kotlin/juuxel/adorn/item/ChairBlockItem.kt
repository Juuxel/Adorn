package juuxel.adorn.item

import io.github.juuxel.polyester.block.PolyesterBlock
import net.minecraft.block.CarpetBlock
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.ActionResult
import net.minecraft.util.math.Direction

class ChairBlockItem(block: PolyesterBlock, settings: Settings) : AdornTallBlockItem(block, settings) {
    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val world = context.world
        val pos = context.blockPos
        if (context.facing == Direction.UP && world.getBlockState(pos).block is CarpetBlock) {
            place(CarpetedTopPlacementContext(context))
            return ActionResult.SUCCESS
        }

        return super.useOnBlock(context)
    }
}

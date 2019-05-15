package juuxel.adorn.item

import io.github.juuxel.polyester.block.PolyesterBlock
import io.github.juuxel.polyester.item.PolyesterItem
import juuxel.adorn.block.entity.CarpetedBlockEntity
import net.minecraft.block.CarpetBlock
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.ActionResult
import net.minecraft.util.math.Direction

class TableBlockItem(block: PolyesterBlock, settings: Settings) : BlockItem(block.unwrap(), settings), PolyesterItem {
    override val name = block.name

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val world = context.world
        val pos = context.blockPos
        val currentBlock = world.getBlockState(pos).block
        if (context.facing == Direction.UP && currentBlock is CarpetBlock) {
            place(CarpetedTopPlacementContext(context))
            val be = world.getBlockEntity(pos) as? CarpetedBlockEntity
            be?.carpet = currentBlock.color
            be?.markDirty()
            return ActionResult.SUCCESS
        }

        return super.useOnBlock(context)
    }
}

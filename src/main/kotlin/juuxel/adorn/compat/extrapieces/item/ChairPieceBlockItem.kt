package juuxel.adorn.compat.extrapieces.item

/*import com.shnupbups.extrapieces.blocks.PieceBlock
import juuxel.adorn.compat.extrapieces.AdornPieces
import juuxel.adorn.item.CarpetedTopPlacementContext
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.CarpetBlock
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.ActionResult
import net.minecraft.util.math.Direction

class ChairPieceBlockItem(pb: PieceBlock, settings: Settings) : AdornPieceBlockItem(pb, settings) {
    // From TallBlockItem.place
    override fun place(context: ItemPlacementContext, state: BlockState): Boolean {
        context.world.setBlockState(context.blockPos.up(), Blocks.AIR.defaultState, 0b11011)
        return super.place(context, state)
    }

    // From ChairBlockItem.useOnBlock
    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val world = context.world
        val pos = context.blockPos
        if (AdornPieces.isCarpetingEnabled(pieceBlock.set) && context.side == Direction.UP && world.getBlockState(pos).block is CarpetBlock) {
            place(CarpetedTopPlacementContext(context))
            return ActionResult.SUCCESS
        }

        return super.useOnBlock(context)
    }
}*/

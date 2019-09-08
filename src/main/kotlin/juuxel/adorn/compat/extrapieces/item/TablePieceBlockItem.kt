package juuxel.adorn.compat.extrapieces.item

import com.shnupbups.extrapieces.blocks.PieceBlock
import juuxel.adorn.compat.extrapieces.AdornPieces
import juuxel.adorn.item.CarpetedTopPlacementContext
import net.minecraft.block.CarpetBlock
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.ActionResult
import net.minecraft.util.math.Direction

class TablePieceBlockItem(pb: PieceBlock, settings: Settings) : AdornPieceBlockItem(pb, settings) {
    // From TableBlockItem.useOnBlock
    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val world = context.world
        val pos = context.blockPos
        if (AdornPieces.isCarpetingEnabled(pieceBlock.set) && context.side == Direction.UP && world.getBlockState(pos).block is CarpetBlock) {
            place(CarpetedTopPlacementContext(context))
            return ActionResult.SUCCESS
        }

        return super.useOnBlock(context)
    }
}

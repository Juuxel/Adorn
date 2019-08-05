package juuxel.adorn.compat.extrapieces.item

import com.shnupbups.extrapieces.blocks.PieceBlock
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.item.ItemPlacementContext

class AdornTallPieceBlockItem(pb: PieceBlock, settings: Settings) : AdornPieceBlockItem(pb, settings) {
    // From TallBlockItem.place
    override fun place(context: ItemPlacementContext, state: BlockState): Boolean {
        context.world.setBlockState(context.blockPos.up(), Blocks.AIR.defaultState, 0b11011)
        return super.place(context, state)
    }
}

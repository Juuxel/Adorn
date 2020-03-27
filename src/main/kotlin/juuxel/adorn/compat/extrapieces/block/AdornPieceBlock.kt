package juuxel.adorn.compat.extrapieces.block

import com.shnupbups.extrapieces.blocks.PieceBlock
import net.minecraft.block.Block

interface AdornPieceBlock : PieceBlock {
    override fun getBlock() = this as Block
}

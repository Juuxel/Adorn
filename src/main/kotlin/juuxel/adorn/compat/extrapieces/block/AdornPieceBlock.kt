package juuxel.adorn.compat.extrapieces.block

import com.shnupbups.extrapieces.blocks.PieceBlock
import juuxel.adorn.compat.extrapieces.AdornPieceMarker
import net.minecraft.block.Block

interface AdornPieceBlock : PieceBlock, AdornPieceMarker {
    override fun getBlock() = this as Block
}

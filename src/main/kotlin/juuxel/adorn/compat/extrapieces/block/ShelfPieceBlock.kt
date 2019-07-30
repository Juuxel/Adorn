package juuxel.adorn.compat.extrapieces.block

import com.shnupbups.extrapieces.core.PieceSet
import juuxel.adorn.block.ShelfBlock
import juuxel.adorn.compat.extrapieces.AdornPieces
import juuxel.adorn.compat.extrapieces.toVariant

class ShelfPieceBlock(private val set: PieceSet) : ShelfBlock(set.toVariant()), AdornPieceBlock {
    override fun getSet() = set

    override fun getType() = AdornPieces.SHELF
}

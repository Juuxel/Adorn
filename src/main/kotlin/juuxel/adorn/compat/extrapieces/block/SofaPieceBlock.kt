package juuxel.adorn.compat.extrapieces.block

import com.shnupbups.extrapieces.core.PieceSet
import juuxel.adorn.block.SofaBlock
import juuxel.adorn.compat.extrapieces.AdornPieces
import juuxel.adorn.compat.extrapieces.toVariant

class SofaPieceBlock(private val set: PieceSet) : SofaBlock(set.toVariant()), AdornPieceBlock {
    override fun getSet() = set

    override fun getType() = AdornPieces.SOFA
}

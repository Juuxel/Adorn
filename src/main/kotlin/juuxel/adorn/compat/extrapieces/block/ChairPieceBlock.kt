package juuxel.adorn.compat.extrapieces.block

import com.shnupbups.extrapieces.core.PieceSet
import juuxel.adorn.block.ChairBlock
import juuxel.adorn.compat.extrapieces.AdornPieces
import juuxel.adorn.compat.extrapieces.toVariant

class ChairPieceBlock(private val set: PieceSet) : ChairBlock(set.toVariant()), AdornPieceBlock {
    override fun isCarpetingEnabled() = false

    override fun getSet() = set

    override fun getType() = AdornPieces.CHAIR
}

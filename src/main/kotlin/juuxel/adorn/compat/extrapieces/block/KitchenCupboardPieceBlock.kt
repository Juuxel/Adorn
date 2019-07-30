package juuxel.adorn.compat.extrapieces.block

import com.shnupbups.extrapieces.core.PieceSet
import juuxel.adorn.block.KitchenCupboardBlock
import juuxel.adorn.compat.extrapieces.AdornPieces
import juuxel.adorn.compat.extrapieces.toVariant

class KitchenCupboardPieceBlock(private val set: PieceSet) : KitchenCupboardBlock(set.toVariant()), AdornPieceBlock {
    override fun getSet() = set

    override fun getType() = AdornPieces.KITCHEN_CUPBOARD
}

package juuxel.adorn.compat.extrapieces.block

import com.shnupbups.extrapieces.core.PieceSet
import juuxel.adorn.block.DrawerBlock
import juuxel.adorn.compat.extrapieces.AdornPieces
import juuxel.adorn.compat.extrapieces.toVariant

class DrawerPieceBlock(private val set: PieceSet) : DrawerBlock(set.toVariant()), AdornPieceBlock {
    override fun getSet() = set

    override fun getType() = AdornPieces.DRAWER
}

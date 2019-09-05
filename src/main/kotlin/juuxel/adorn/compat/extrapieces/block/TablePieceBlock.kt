package juuxel.adorn.compat.extrapieces.block

import com.shnupbups.extrapieces.core.PieceSet
import juuxel.adorn.block.TableBlock
import juuxel.adorn.compat.extrapieces.AdornPieces
import juuxel.adorn.compat.extrapieces.toVariant

class TablePieceBlock(private val set: PieceSet) : TableBlock(set.toVariant()), AdornPieceBlock {
    override fun isCarpetingEnabled() = false

    override fun getSet() = set

    override fun getType() = AdornPieces.TABLE
}

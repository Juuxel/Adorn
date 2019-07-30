package juuxel.adorn.compat.extrapieces.block

import com.shnupbups.extrapieces.core.PieceSet
import juuxel.adorn.block.PlatformBlock
import juuxel.adorn.compat.extrapieces.AdornPieces
import juuxel.adorn.compat.extrapieces.toVariant

class PlatformPieceBlock(private val set: PieceSet) : PlatformBlock(set.toVariant()), AdornPieceBlock {
    override fun getSet() = set

    override fun getType() = AdornPieces.PLATFORM
}

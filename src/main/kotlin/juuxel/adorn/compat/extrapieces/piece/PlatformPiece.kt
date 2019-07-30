package juuxel.adorn.compat.extrapieces.piece

import com.shnupbups.extrapieces.core.PieceSet
import juuxel.adorn.Adorn
import juuxel.adorn.compat.extrapieces.block.PlatformPieceBlock

object PlatformPiece : SimplePiece(Adorn.id("platform"), Adorn.id("block/templates/platform")) {
    override fun getNew(set: PieceSet) = PlatformPieceBlock(set)
}

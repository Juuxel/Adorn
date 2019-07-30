package juuxel.adorn.compat.extrapieces.piece

import com.shnupbups.extrapieces.core.PieceSet
import juuxel.adorn.Adorn
import juuxel.adorn.compat.extrapieces.block.PostPieceBlock

object PostPiece : SimplePiece(Adorn.id("post"), Adorn.id("block/templates/post")) {
    override fun getNew(set: PieceSet) = PostPieceBlock(set)
}

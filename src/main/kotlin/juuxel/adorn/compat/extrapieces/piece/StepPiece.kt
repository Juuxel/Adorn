package juuxel.adorn.compat.extrapieces.piece

import com.shnupbups.extrapieces.core.PieceSet
import juuxel.adorn.Adorn
import juuxel.adorn.compat.extrapieces.block.StepPieceBlock

object StepPiece : SimplePiece(Adorn.id("step"), Adorn.id("block/templates/step")) {
    override fun getNew(set: PieceSet) = StepPieceBlock(set)
}

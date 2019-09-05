package juuxel.adorn.compat.extrapieces.piece

import com.shnupbups.extrapieces.core.PieceSet
import com.shnupbups.extrapieces.core.PieceTypes
import com.shnupbups.extrapieces.recipe.ShapedPieceRecipe
import juuxel.adorn.Adorn
import juuxel.adorn.compat.extrapieces.AdornPieces
import juuxel.adorn.compat.extrapieces.block.PlatformPieceBlock

object PlatformPiece : SimplePiece(Adorn.id("platform"), Adorn.id("block/templates/platform")) {
    override fun getNew(set: PieceSet) = PlatformPieceBlock(set)

    override fun getRecipes() = arrayListOf(
        ShapedPieceRecipe(this, 2, "S", "#")
            .addToKey('S', PieceTypes.SLAB)
            .addToKey('#', AdornPieces.POST)
    )
}

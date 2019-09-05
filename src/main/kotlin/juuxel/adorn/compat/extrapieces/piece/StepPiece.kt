package juuxel.adorn.compat.extrapieces.piece

import com.shnupbups.extrapieces.core.PieceSet
import com.shnupbups.extrapieces.core.PieceTypes
import com.shnupbups.extrapieces.recipe.ShapedPieceRecipe
import juuxel.adorn.Adorn
import juuxel.adorn.compat.extrapieces.block.StepPieceBlock
import juuxel.adorn.lib.AdornTags
import net.minecraft.item.Items

object StepPiece : SimplePiece(Adorn.id("step"), Adorn.id("block/templates/step")) {
    override fun getNew(set: PieceSet) = StepPieceBlock(set)

    override fun getRecipes() = arrayListOf(
        ShapedPieceRecipe(this, 1, "S", "#")
            .addToKey('S', PieceTypes.SLAB)
            .addToKey('#', Items.STICK)
            .addToKey('#', AdornTags.STONE_ROD)
    )
}

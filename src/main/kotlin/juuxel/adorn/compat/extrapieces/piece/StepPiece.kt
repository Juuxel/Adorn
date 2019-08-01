package juuxel.adorn.compat.extrapieces.piece

import com.shnupbups.extrapieces.core.PieceSet
import com.shnupbups.extrapieces.core.PieceTypes
import com.shnupbups.extrapieces.recipe.ShapedPieceRecipe
import juuxel.adorn.Adorn
import juuxel.adorn.compat.extrapieces.block.StepPieceBlock
import juuxel.adorn.compat.extrapieces.item.AdornPieceRecipe
import net.minecraft.item.Items
import net.minecraft.util.Identifier

object StepPiece : SimplePiece(Adorn.id("step"), Adorn.id("block/templates/step")) {
    override fun getNew(set: PieceSet) = StepPieceBlock(set)

    override fun getRecipes() = arrayListOf<ShapedPieceRecipe>(
        AdornPieceRecipe(this, 1, "S", "#")
            .addToKey('S', PieceTypes.SLAB)
            .addToKey('#', Items.STICK)
            .addTagToKey('#', Identifier("c", "stone_rod"))
    )
}

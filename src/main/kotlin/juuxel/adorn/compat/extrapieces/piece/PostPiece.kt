package juuxel.adorn.compat.extrapieces.piece

import com.shnupbups.extrapieces.core.PieceSet
import com.shnupbups.extrapieces.core.PieceTypes
import com.shnupbups.extrapieces.recipe.ShapedPieceRecipe
import juuxel.adorn.Adorn
import juuxel.adorn.compat.extrapieces.block.PostPieceBlock
import juuxel.adorn.compat.extrapieces.item.AdornPieceRecipe
import net.minecraft.item.Items
import net.minecraft.util.Identifier

object PostPiece : SimplePiece(Adorn.id("post"), Adorn.id("block/templates/post")) {
    override fun getNew(set: PieceSet) = PostPieceBlock(set)

    override fun getRecipes() = arrayListOf<ShapedPieceRecipe>(
        AdornPieceRecipe(this, 4, "#", "#", "-")
            .addToKey('-', PieceTypes.BASE)
            .addToKey('#', Items.STICK)
            .addTagToKey('#', Identifier("c", "stone_rod"))
    )

    override fun getStonecuttingCount() = 2
}

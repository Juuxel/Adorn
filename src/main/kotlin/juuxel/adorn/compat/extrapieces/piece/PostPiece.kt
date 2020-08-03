package juuxel.adorn.compat.extrapieces.piece

import com.shnupbups.extrapieces.core.PieceSet
import com.shnupbups.extrapieces.core.PieceTypes
import com.shnupbups.extrapieces.recipe.ShapedPieceRecipe
import juuxel.adorn.Adorn
import juuxel.adorn.compat.extrapieces.block.PostPieceBlock
import juuxel.adorn.lib.AdornTags
import net.minecraft.item.Items

object PostPiece : SimpleSidedPiece(Adorn.id("post"), Adorn.id("block/templates/post")) {
    override fun getNew(set: PieceSet) = PostPieceBlock(set)

    override fun getRecipes() = arrayListOf(
        ShapedPieceRecipe(this, 4, "#", "#", "-")
            .addToKey('-', PieceTypes.BASE)
            .addToKey('#', Items.STICK)
            .addToKey('#', AdornTags.STONE_ROD)
    )

    override fun getStonecuttingCount() = 2
}

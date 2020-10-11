package juuxel.adorn.compat.extrapieces.piece

import com.shnupbups.extrapieces.blocks.PieceBlock
import com.shnupbups.extrapieces.core.PieceSet
import com.shnupbups.extrapieces.core.PieceTypes
import com.shnupbups.extrapieces.recipe.ShapedPieceRecipe
import com.swordglowsblue.artifice.api.ArtificeResourcePack
import juuxel.adorn.Adorn
import juuxel.adorn.compat.extrapieces.block.ShelfPieceBlock
import juuxel.adorn.compat.extrapieces.getRegistryId
import juuxel.adorn.lib.AdornTags
import net.minecraft.item.Items

object ShelfPiece : SimpleRotatingPiece(Adorn.id("shelf")) {
    private val MODEL_PARENT = Adorn.id("block/templates/shelf")

    override fun getNew(set: PieceSet) = ShelfPieceBlock(set)

    override fun addBlockModels(pack: ArtificeResourcePack.ClientResourcePackBuilder, pb: PieceBlock) {
        val id = pb.getRegistryId()
        pack.addBlockModel(id) {
            it.parent(MODEL_PARENT)
            it.texture("texture", pb.set.mainTexture)
            it.texture("supports", pb.set.mainTexture)
        }
    }

    override fun getTagId() = Adorn.id("shelves")

    override fun getRecipes() = arrayListOf(
        ShapedPieceRecipe(this, 3, "SSS", "/ /")
            .addToKey('S', PieceTypes.SLAB)
            .addToKey('/', Items.STICK)
            .addToKey('/', AdornTags.STONE_RODS)
    )
}

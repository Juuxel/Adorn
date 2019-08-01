package juuxel.adorn.compat.extrapieces.piece

import com.shnupbups.extrapieces.ExtraPieces
import com.shnupbups.extrapieces.blocks.PieceBlock
import com.shnupbups.extrapieces.core.PieceSet
import com.shnupbups.extrapieces.core.PieceType
import com.shnupbups.extrapieces.core.PieceTypes
import com.shnupbups.extrapieces.recipe.ShapedPieceRecipe
import com.swordglowsblue.artifice.api.ArtificeResourcePack
import juuxel.adorn.Adorn
import juuxel.adorn.compat.extrapieces.AdornPieces
import juuxel.adorn.compat.extrapieces.block.KitchenCounterPieceBlock
import juuxel.adorn.compat.extrapieces.getRegistryId
import juuxel.adorn.compat.extrapieces.item.AdornPieceRecipe
import net.minecraft.item.Items
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction

object KitchenCounterPiece : AdornPiece(Adorn.id("kitchen_counter")) {
    private val MODEL_PARENT = Adorn.id("block/templates/kitchen_counter")

    override fun getNew(set: PieceSet) = KitchenCounterPieceBlock(set)

    override fun addBlockstate(pack: ArtificeResourcePack.ClientResourcePackBuilder, pb: PieceBlock) {
        val id = pb.getRegistryId()
        fun getYRotation(facing: Direction): Int = when (facing) {
            Direction.EAST -> 0
            Direction.SOUTH -> 90
            Direction.WEST -> 180
            Direction.NORTH -> 270

            else -> 0
        }

        pack.addBlockState(id) { state ->
            val baseModelId = ExtraPieces.prependToPath(id, "block/")

            for (facing in AdornPieces.HORIZONTAL_DIRECTIONS) {
                state.multipartCase { case ->
                    case.`when`("facing", facing.asString())

                    case.apply {
                        it.model(baseModelId)
                        it.rotationY(getYRotation(facing))
                        it.uvlock(true)
                    }
                }

                state.multipartCase { case ->
                    case.`when`("facing", facing.asString())
                    case.`when`("front", "left")

                    case.apply {
                        it.model(ExtraPieces.appendToPath(baseModelId, "_connection_left"))
                        it.rotationY(getYRotation(facing))
                    }
                }

                state.multipartCase { case ->
                    case.`when`("facing", facing.asString())
                    case.`when`("front", "right")

                    case.apply {
                        it.model(ExtraPieces.appendToPath(baseModelId, "_connection_right"))
                        it.rotationY(getYRotation(facing))
                    }
                }
            }
        }
    }

    override fun addBlockModels(pack: ArtificeResourcePack.ClientResourcePackBuilder, pb: PieceBlock) {
        addBlockModel(pack, pb, "")
        addBlockModel(pack, pb, "_connection_left")
        addBlockModel(pack, pb, "_connection_right")
    }

    override fun addBlockModel(
        pack: ArtificeResourcePack.ClientResourcePackBuilder, pb: PieceBlock, append: String
    ) {
        val id = pb.getRegistryId()
        pack.addBlockModel(ExtraPieces.appendToPath(id, append)) {
            it.parent(ExtraPieces.appendToPath(MODEL_PARENT, append))
            it.texture("planks", pb.set.mainTexture)
        }
    }

    override fun getRecipes() = arrayListOf<ShapedPieceRecipe>(
        AdornPieceRecipe(this, 3, "SS", "##")
            .addToKey('S', Items.POLISHED_ANDESITE_SLAB)
            .addToKey('#', PieceTypes.BASE)
    )
}

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
import juuxel.adorn.compat.extrapieces.block.ChairPieceBlock
import juuxel.adorn.compat.extrapieces.getRegistryId
import juuxel.adorn.compat.extrapieces.item.AdornPieceBlockItem
import juuxel.adorn.compat.extrapieces.item.AdornPieceRecipe
import juuxel.adorn.compat.extrapieces.item.AdornTallPieceBlockItem
import net.minecraft.block.enums.DoubleBlockHalf
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction

object ChairPiece : AdornPiece(Adorn.id("chair")) {
    private val UPPER_MODEL_PARENT = Adorn.id("block/templates/chair_upper")
    private val LOWER_MODEL_PARENT = Adorn.id("block/templates/chair_lower")
    private val ITEM_MODEL_PARENT = Adorn.id("item/templates/chair")

    override fun getNew(set: PieceSet) = ChairPieceBlock(set)

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
            for (half in DoubleBlockHalf.values()) {
                for (facing in AdornPieces.HORIZONTAL_DIRECTIONS) {
                    state.multipartCase { case ->
                        case.`when`("half", half.asString())
                        case.`when`("facing", facing.asString())

                        case.apply {
                            it.model(ExtraPieces.prependToPath(ExtraPieces.appendToPath(id, "_" + half.asString()), "block/"))
                            it.rotationY(getYRotation(facing))
                        }
                    }
                }
            }
        }
    }

    override fun addBlockModels(pack: ArtificeResourcePack.ClientResourcePackBuilder, pb: PieceBlock) {
        val id = pb.getRegistryId()
        pack.addBlockModel(ExtraPieces.appendToPath(id, "_upper")) {
            it.parent(UPPER_MODEL_PARENT)
            it.texture("planks", pb.set.mainTexture)
        }
        pack.addBlockModel(ExtraPieces.appendToPath(id, "_lower")) {
            it.parent(LOWER_MODEL_PARENT)
            it.texture("planks", pb.set.mainTexture)
        }
    }

    override fun addItemModel(pack: ArtificeResourcePack.ClientResourcePackBuilder, pb: PieceBlock) {
        val id = pb.getRegistryId()
        pack.addItemModel(id) {
            it.parent(ITEM_MODEL_PARENT)
            it.texture("planks", pb.set.mainTexture)
        }
    }

    override fun getRecipes() = arrayListOf<ShapedPieceRecipe>(
        AdornPieceRecipe(this, 2, " S", "SS", "##")
            .addToKey('S', PieceTypes.SLAB)
            .addToKey('#', Items.STICK)
            .addTagToKey('#', Identifier("c", "stone_rod"))
    )

    override fun getBlockItem(pb: PieceBlock) = AdornTallPieceBlockItem(pb, Item.Settings())
}

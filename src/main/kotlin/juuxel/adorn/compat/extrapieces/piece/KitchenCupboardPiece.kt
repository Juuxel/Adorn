package juuxel.adorn.compat.extrapieces.piece

/*import com.shnupbups.extrapieces.ExtraPieces
import com.shnupbups.extrapieces.blocks.PieceBlock
import com.shnupbups.extrapieces.core.PieceSet
import com.swordglowsblue.artifice.api.ArtificeResourcePack
import juuxel.adorn.Adorn
import juuxel.adorn.compat.extrapieces.AdornPieces
import juuxel.adorn.compat.extrapieces.block.KitchenCupboardPieceBlock
import juuxel.adorn.compat.extrapieces.getRegistryId
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction

object KitchenCupboardPiece : AdornPiece(Adorn.id("kitchen_cupboard")) {
    private val MODEL_PARENT = Adorn.id("block/templates/kitchen_cupboard_door")
    private val ITEM_MODEL_PARENT = Adorn.id("item/templates/kitchen_cupboard")

    override fun getNew(set: PieceSet) = KitchenCupboardPieceBlock(set)

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
            val counterId = Identifier(baseModelId.namespace, baseModelId.path.substringBeforeLast("cupboard") + "counter")

            for (facing in AdornPieces.HORIZONTAL_DIRECTIONS) {
                state.multipartCase { case ->
                    case.`when`("facing", facing.asString())

                    case.apply {
                        it.model(counterId)
                        it.rotationY(getYRotation(facing))
                        it.uvlock(true)
                    }
                }

                state.multipartCase { case ->
                    case.`when`("facing", facing.asString())

                    case.apply {
                        it.model(ExtraPieces.appendToPath(baseModelId, "_door"))
                        it.rotationY(getYRotation(facing))
                    }
                }
            }
        }
    }

    override fun addBlockModels(pack: ArtificeResourcePack.ClientResourcePackBuilder, pb: PieceBlock) {
        val id = pb.getRegistryId()
        pack.addBlockModel(ExtraPieces.appendToPath(id, "_door")) {
            it.parent(MODEL_PARENT)
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

    override fun getStonecuttingCount() = 0
}*/

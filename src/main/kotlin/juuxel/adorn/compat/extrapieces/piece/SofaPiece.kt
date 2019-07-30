package juuxel.adorn.compat.extrapieces.piece

import com.shnupbups.extrapieces.ExtraPieces
import com.shnupbups.extrapieces.blocks.PieceBlock
import com.shnupbups.extrapieces.core.PieceSet
import com.shnupbups.extrapieces.core.PieceType
import com.swordglowsblue.artifice.api.ArtificeResourcePack
import juuxel.adorn.Adorn
import juuxel.adorn.compat.extrapieces.AdornPieces
import juuxel.adorn.compat.extrapieces.block.SofaPieceBlock
import juuxel.adorn.compat.extrapieces.getRegistryId
import net.minecraft.util.math.Direction

object SofaPiece : PieceType(Adorn.id("sofa")) {
    private val MODEL_PARENT = Adorn.id("block/templates/sofa")
    private val ITEM_MODEL_PARENT = Adorn.id("item/templates/sofa")

    override fun getNew(set: PieceSet) = SofaPieceBlock(set)

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
            for (facing in AdornPieces.HORIZONTAL_DIRECTIONS) {
                state.multipartCase { case ->
                    case.`when`("facing", facing.asString())
                    case.apply {
                        it.model(ExtraPieces.prependToPath(ExtraPieces.appendToPath(id, "_center"), "block/"))
                        it.rotationY(getYRotation(facing))
                        it.uvlock(true)
                    }
                }

                state.multipartCase { case ->
                    case.`when`("facing", facing.asString())
                    case.`when`("connected_left", "false")
                    case.`when`("front", "none")

                    case.apply {
                        it.model(ExtraPieces.prependToPath(ExtraPieces.appendToPath(id, "_arm_left"), "block/"))
                        it.rotationY(getYRotation(facing))
                    }
                }

                state.multipartCase { case ->
                    case.`when`("facing", facing.asString())
                    case.`when`("connected_right", "false")
                    case.`when`("front", "none")

                    case.apply {
                        it.model(ExtraPieces.prependToPath(ExtraPieces.appendToPath(id, "_arm_right"), "block/"))
                        it.rotationY(getYRotation(facing))
                    }
                }

                state.multipartCase { case ->
                    case.`when`("facing", facing.asString())
                    case.`when`("front", "left")

                    case.apply {
                        it.model(ExtraPieces.prependToPath(ExtraPieces.appendToPath(id, "_corner_left"), "block/"))
                        it.rotationY(getYRotation(facing))
                        it.uvlock(true)
                    }
                }

                state.multipartCase { case ->
                    case.`when`("facing", facing.asString())
                    case.`when`("front", "right")

                    case.apply {
                        it.model(ExtraPieces.prependToPath(ExtraPieces.appendToPath(id, "_corner_right"), "block/"))
                        it.rotationY(getYRotation(facing))
                        it.uvlock(true)
                    }
                }
            }
        }
    }

    override fun addBlockModels(pack: ArtificeResourcePack.ClientResourcePackBuilder, pb: PieceBlock) {
        addBlockModel(pack, pb, "center")
        addBlockModel(pack, pb, "arm_left")
        addBlockModel(pack, pb, "arm_right")
        addBlockModel(pack, pb, "corner_left")
        addBlockModel(pack, pb, "corner_right")
    }

    override fun addBlockModel(
        pack: ArtificeResourcePack.ClientResourcePackBuilder, pb: PieceBlock, append: String
    ) {
        val id = pb.getRegistryId()
        pack.addBlockModel(ExtraPieces.appendToPath(id, "_$append")) {
            it.parent(ExtraPieces.appendToPath(MODEL_PARENT, "_$append"))
            it.texture("wool", pb.set.mainTexture)
        }
    }

    override fun addItemModel(pack: ArtificeResourcePack.ClientResourcePackBuilder, pb: PieceBlock) {
        val id = pb.getRegistryId()
        pack.addItemModel(id) {
            it.parent(ITEM_MODEL_PARENT)
            it.texture("wool", pb.set.mainTexture)
        }
    }
}

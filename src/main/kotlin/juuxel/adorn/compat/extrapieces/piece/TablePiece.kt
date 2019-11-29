package juuxel.adorn.compat.extrapieces.piece

/*import com.shnupbups.extrapieces.ExtraPieces
import com.shnupbups.extrapieces.blocks.PieceBlock
import com.shnupbups.extrapieces.core.PieceSet
import com.shnupbups.extrapieces.core.PieceTypes
import com.shnupbups.extrapieces.recipe.ShapedPieceRecipe
import com.swordglowsblue.artifice.api.ArtificeResourcePack
import juuxel.adorn.Adorn
import juuxel.adorn.compat.extrapieces.AdornPieces
import juuxel.adorn.compat.extrapieces.AdornPiecesClient
import juuxel.adorn.compat.extrapieces.block.TablePieceBlock
import juuxel.adorn.compat.extrapieces.getRegistryId
import juuxel.adorn.compat.extrapieces.item.TablePieceBlockItem
import juuxel.adorn.lib.AdornTags
import net.minecraft.item.Item
import net.minecraft.item.Items

object TablePiece : AdornPiece(Adorn.id("table")) {
    private val MODEL_PARENT = Adorn.id("block/templates/table")
    private val LEG_MODEL_PARENT = Adorn.id("block/templates/table_leg")
    private val ITEM_MODEL_PARENT = Adorn.id("item/templates/table")

    override fun getNew(set: PieceSet) = TablePieceBlock(set)

    override fun addBlockModels(pack: ArtificeResourcePack.ClientResourcePackBuilder, pb: PieceBlock) {
        val id = pb.getRegistryId()
        pack.addBlockModel(id) {
            it.parent(MODEL_PARENT)
            it.texture("planks", pb.set.mainTexture)
        }
        pack.addBlockModel(ExtraPieces.appendToPath(id, "_leg")) {
            it.parent(LEG_MODEL_PARENT)
            it.texture("log", pb.set.mainTexture)
        }
    }

    override fun addBlockstate(pack: ArtificeResourcePack.ClientResourcePackBuilder, pb: PieceBlock) {
        val id = pb.getRegistryId()
        pack.addBlockState(id) { state ->
            state.multipartCase { case -> case.apply { it.model(ExtraPieces.prependToPath(id, "block/")) } }

            // No connections == all legs
            for (rotation in intArrayOf(0, 90, 180, 270)) {
                state.multipartCase { case ->
                    case.`when`("north", "false")
                    case.`when`("east", "false")
                    case.`when`("south", "false")
                    case.`when`("west", "false")

                    case.apply {
                        it.model(ExtraPieces.prependToPath(ExtraPieces.appendToPath(id, "_leg"), "block/"))
                        it.rotationY(rotation)
                        it.uvlock(true)
                    }
                }
            }

            // Ends
            for (facing in AdornPieces.HORIZONTAL_DIRECTIONS) {
                val others = AdornPieces.HORIZONTAL_DIRECTIONS - facing
                val map = (others.map { it.asString() to "false" } + (facing.asString() to "true")).toMap()

                for ((x, z) in getEdgeLegRotations(facing.asString())) {
                    state.multipartCase { case ->
                        for ((key, value) in map) case.`when`(key, value)

                        case.apply {
                            it.model(ExtraPieces.prependToPath(ExtraPieces.appendToPath(id, "_leg"), "block/"))
                            it.rotationY(getLegRotation(x, z))
                            it.uvlock(true)
                        }
                    }
                }
            }

            val ns = arrayOf("north", "south")
            val we = arrayOf("west", "east")

            // Corners
            for ((nsIndex, connectionNS) in ns.withIndex()) {
                for ((weIndex, connectionWE) in we.withIndex()) {
                    state.multipartCase { case ->
                        case.`when`(connectionNS, "true")
                        case.`when`(connectionWE, "true")
                        case.`when`(ns[1 - nsIndex], "false")
                        case.`when`(we[1 - weIndex], "false")

                        case.apply {
                            it.model(ExtraPieces.prependToPath(ExtraPieces.appendToPath(id, "_leg"), "block/"))
                            it.rotationY(getCornerLegRotation(connectionNS, connectionWE))
                            it.uvlock(true)
                        }
                    }
                }
            }

            AdornPiecesClient.addCarpets(state, pb.set)
        }
    }

    override fun addItemModel(pack: ArtificeResourcePack.ClientResourcePackBuilder, pb: PieceBlock) {
        val id = pb.getRegistryId()
        pack.addItemModel(id) {
            it.parent(ITEM_MODEL_PARENT)
            it.texture("planks", pb.set.mainTexture)
            it.texture("log", pb.set.mainTexture)
        }
    }

    private fun getEdgeLegRotations(facing: String): Set<Pair<Int, Int>> = when (facing) {
        "north" -> setOf((0 to 1), (1 to 1))
        "south" -> setOf((0 to 0), (1 to 0))
        "east" -> setOf((0 to 0), (0 to 1))
        "west" -> setOf((1 to 0), (1 to 1))
        else -> throw IllegalArgumentException("unknown facing: $facing")
    }

    private fun getCornerLegRotation(ns: String, we: String): Int = when (ns) {
        "north" -> when (we) {
            "west" -> getLegRotation(1, 1)
            "east" -> getLegRotation(0, 1)
            else -> throw IllegalArgumentException()
        }

        "south" -> when (we) {
            "west" -> getLegRotation(1, 0)
            "east" -> getLegRotation(0, 0)
            else -> throw IllegalArgumentException()
        }

        else -> throw IllegalArgumentException()
    }

    private fun getLegRotation(x: Int, z: Int): Int = when {
        x == 0 && z == 0 -> 0
        x == 1 && z == 0 -> 90
        x == 1 && z == 1 -> 180
        x == 0 && z == 1 -> 270

        else -> 0
    }

    override fun getRecipes() = arrayListOf(
        ShapedPieceRecipe(this, 3, "---", "| |", "| |")
            .addToKey('-', PieceTypes.SLAB)
            .addToKey('|', Items.STICK)
            .addToKey('|', AdornTags.STONE_ROD)
    )

    override fun getBlockItem(pb: PieceBlock) = TablePieceBlockItem(pb, Item.Settings())
}*/

package juuxel.adorn.json.kitchen

import io.github.cottonmc.jsonfactory.data.BlockStateProperty
import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.output.model.ModelVariant
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState.*
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin

object KitchenCupboardBlockState : AbstractContentGenerator("kitchen_cupboard.block_state", "blockstates",
    AdornPlugin.KITCHEN
) {
    override fun generate(id: Identifier) = listOf(
        MultipartBlockState(
            multipart = BlockStateProperty.horizontalFacing.values.flatMap {
                listOf(
                    Multipart(
                        `when` = When("facing", it),
                        apply = ModelVariant(
                            model = id.wrapPath("block/", "_kitchen_counter"),
                            y = getYRotation(it),
                            uvlock = true
                        )
                    ),
                    Multipart(
                        `when` = When("facing", it),
                        apply = ModelVariant(
                            model = id.wrapPath("block/", "_kitchen_cupboard_door"),
                            y = getYRotation(it)
                        )
                    )
                )
            }
        ).suffixed("kitchen_cupboard")
    )

    private fun getYRotation(facing: String): Int = when (facing) {
        "east" -> 0
        "south" -> 90
        "west" -> 180
        "north" -> 270

        else -> 0
    }
}

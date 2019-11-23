package juuxel.adorn.json.kitchen

import io.github.cottonmc.jsonfactory.data.BlockStateProperty
import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.output.model.ModelVariant
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState.Multipart
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState.When
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin

object KitchenSinkBlockState : AbstractContentGenerator("kitchen_sink.block_state", "blockstates",
    AdornPlugin.KITCHEN
) {
    override fun generate(id: Identifier) = listOf(
        MultipartBlockState(
            multipart = BlockStateProperty.horizontalFacing.values.flatMap { facing ->
                listOf(
                    Multipart(
                        `when` = When("facing", facing),
                        apply = ModelVariant(
                            model = id.wrapPath("block/", "_kitchen_sink"),
                            y = getYRotation(facing),
                            uvlock = true
                        )
                    ),
                    Multipart(
                        `when` = When(mapOf("facing" to facing, "front" to "left")),
                        apply = ModelVariant(
                            model = id.wrapPath("block/", "_kitchen_counter_connection_left"),
                            y = getYRotation(facing)
                        )
                    ),
                    Multipart(
                        `when` = When(mapOf("facing" to facing, "front" to "right")),
                        apply = ModelVariant(
                            model = id.wrapPath("block/", "_kitchen_counter_connection_right"),
                            y = getYRotation(facing)
                        )
                    ),
                    Multipart(
                        `when` = When(mapOf("facing" to facing, "waterlogged" to "true")),
                        apply = ModelVariant(
                            model = Identifier("adorn", "block/kitchen_sink_water"),
                            y = getYRotation(facing)
                        )
                    )
                )
            }
        ).suffixed("kitchen_sink")
    )

    private fun getYRotation(facing: String): Int = when (facing) {
        "east" -> 0
        "south" -> 90
        "west" -> 180
        "north" -> 270

        else -> 0
    }
}

package juuxel.adorn.json.chair

import io.github.cottonmc.jsonfactory.data.BlockStateProperty
import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.output.model.ModelVariant
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState.Multipart
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState.When
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin

object ChairBlockState : AbstractContentGenerator("chair.block_state", "blockstates",
    AdornPlugin.CHAIR
) {
    override fun generate(id: Identifier) = listOf(
        MultipartBlockState(
            multipart = sequence {
                for (half in BlockStateProperty.halfUL.values) {
                    for (facing in BlockStateProperty.horizontalFacing.values) {
                        yield(
                            Multipart(
                                `when` = When(mapOf("half" to half, "facing" to facing)),
                                apply = ModelVariant(
                                    id.wrapPath("block/", "_chair_$half"),
                                    y = getYRotation(facing)
                                )
                            )
                        )
                    }
                }

                val colors = arrayOf(
                    "red",
                    "black",
                    "green",
                    "brown",
                    "blue",
                    "purple",
                    "cyan",
                    "light_gray",
                    "gray",
                    "pink",
                    "lime",
                    "yellow",
                    "light_blue",
                    "magenta",
                    "orange",
                    "white"
                )

                // Carpets
                for (color in colors) {
                    yield(
                        Multipart(
                            `when` = When("carpet", color),
                            apply = ModelVariant(
                                model = Identifier.mc("block/${color}_carpet")
                            )
                        )
                    )
                }
            }.toList()
        ).suffixed("chair")
    )

    private fun getYRotation(facing: String): Int = when (facing) {
        "east" -> 0
        "south" -> 90
        "west" -> 180
        "north" -> 270

        else -> 0
    }
}

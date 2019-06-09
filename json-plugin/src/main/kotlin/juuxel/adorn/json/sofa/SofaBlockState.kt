package juuxel.adorn.json.sofa

import io.github.cottonmc.jsonfactory.data.BlockStateProperty
import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.output.model.ModelVariant
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState.Multipart
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState.When
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin

object SofaBlockState : AbstractContentGenerator("sofa.block_state", "blockstates", AdornPlugin.SOFA) {
    override fun generate(id: Identifier) = listOf(
        MultipartBlockState(
            multipart = sequence {
                for (facing in BlockStateProperty.horizontalFacing.values) {

                    yield(
                        Multipart(
                            `when` = When("facing", facing),
                            apply = ModelVariant(
                                model = id.wrapPath("block/", "_sofa_center"),
                                y = getYRotation(facing),
                                uvlock = true
                            )
                        )
                    )

                    yield(
                        Multipart(
                            `when` = When(mapOf(
                                "facing" to facing,
                                "connected_left" to "false",
                                "front" to "none"
                            )),
                            apply = ModelVariant(
                                model = id.wrapPath("block/", "_sofa_arm_left"),
                                y = getYRotation(facing)
                            )
                        )
                    )

                    yield(
                        Multipart(
                            `when` = When(mapOf(
                                "facing" to facing,
                                "connected_right" to "false",
                                "front" to "none"
                            )),
                            apply = ModelVariant(
                                model = id.wrapPath("block/", "_sofa_arm_right"),
                                y = getYRotation(facing)
                            )
                        )
                    )

                    yield(
                        Multipart(
                            `when` = When(mapOf(
                                "facing" to facing,
                                "front" to "left"
                            )),
                            apply = ModelVariant(
                                model = id.wrapPath("block/", "_sofa_corner_left"),
                                y = getYRotation(facing),
                                uvlock = true
                            )
                        )
                    )

                    yield(
                        Multipart(
                            `when` = When(mapOf(
                                "facing" to facing,
                                "front" to "right"
                            )),
                            apply = ModelVariant(
                                model = id.wrapPath("block/", "_sofa_corner_right"),
                                y = getYRotation(facing),
                                uvlock = true
                            )
                        )
                    )
                }
            }.toList()
        ).suffixed("sofa")
    )

    private fun getYRotation(facing: String): Int = when (facing) {
        "east" -> 0
        "south" -> 90
        "west" -> 180
        "north" -> 270

        else -> 0
    }
}

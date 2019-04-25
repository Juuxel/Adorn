package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.data.BlockStateProperty
import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.ContentGenerator
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState.Multipart
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState.When
import io.github.cottonmc.jsonfactory.output.suffixed
import io.github.cottonmc.jsonfactory.output.model.ModelBlockState.Variant

object SofaBlockState : ContentGenerator("Sofa Block State", "blockstates", AdornPlugin.SOFA) {
    override fun generate(id: Identifier) = listOf(
        MultipartBlockState(
            multipart = sequence {
                for (facing in BlockStateProperty.horizontalFacing.values) {

                    yield(
                        Multipart(
                            `when` = When("facing", facing),
                            apply = Variant(
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
                            apply = Variant(
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
                            apply = Variant(
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
                            apply = Variant(
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
                            apply = Variant(
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

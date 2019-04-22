package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.ContentGenerator
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState.Multipart
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState.When
import io.github.cottonmc.jsonfactory.output.suffixed
import io.github.cottonmc.jsonfactory.output.model.ModelBlockState.Variant
import net.minecraft.util.math.Direction

object SofaBlockState : ContentGenerator("Sofa Block State", "blockstates", AdornPlugin.SOFA) {
    override fun generate(id: Identifier) = listOf(
        MultipartBlockState(
            multipart = sequence {
                for (facing in Direction.values()) {
                    if (facing.horizontal == -1) continue

                    yield(
                        Multipart(
                            `when` = When("facing", facing.getName()),
                            apply = Variant(
                                model = id.wrapPath("block/", "_sofa_center"),
                                y = getYRotation(facing)
                            )
                        )
                    )

                    yield(
                        Multipart(
                            `when` = When(mapOf(
                                "facing" to facing.getName(),
                                "connected_left" to "false"
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
                                "facing" to facing.getName(),
                                "connected_right" to "false"
                            )),
                            apply = Variant(
                                model = id.wrapPath("block/", "_sofa_arm_right"),
                                y = getYRotation(facing)
                            )
                        )
                    )
                }
            }.toList()
        ).suffixed("sofa")
    )

    private fun getYRotation(direction: Direction): Int = when (direction) {
        Direction.EAST -> 0
        Direction.SOUTH -> 90
        Direction.WEST -> 180
        Direction.NORTH -> 270

        else -> 0
    }
}

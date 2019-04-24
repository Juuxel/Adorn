package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.data.BlockStateProperty
import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.ContentGenerator
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState.Multipart
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState.When
import io.github.cottonmc.jsonfactory.output.suffixed
import io.github.cottonmc.jsonfactory.output.model.ModelBlockState.Variant

object TableBlockState : ContentGenerator("Table Block State", "blockstates", AdornPlugin.TABLE) {
    override fun generate(id: Identifier) = listOf(
        MultipartBlockState(
            multipart = sequence {
                yield(
                    Multipart(
                        apply = Variant(model = id.wrapPath("block/", "_table"))
                    )
                )

                // No connections == all legs
                for (rotation in intArrayOf(0, 90, 180, 270)) {
                    yield(
                        Multipart(
                            `when` = When(
                                mapOf("north" to "false", "east" to "false", "south" to "false", "west" to "false")
                            ),
                            apply = Variant(
                                model = id.wrapPath("block/", "_table_leg"),
                                y = rotation,
                                uvlock = true
                            )
                        )
                    )
                }

                // Ends
                for (facing in BlockStateProperty.horizontalFacing.values) {
                    val others = BlockStateProperty.horizontalFacing.values - facing
                    val map = (others.map { it to "false" } + (facing to "true")).toMap()

                    for ((x, z) in getEdgeLegRotations(facing)) {
                        yield(
                            Multipart(
                                `when` = When(map),
                                apply = Variant(
                                    model = id.wrapPath("block/", "_table_leg"),
                                    y = getLegRotation(x, z),
                                    uvlock = true
                                )
                            )
                        )
                    }
                }

                val ns = arrayOf("north", "south")
                val we = arrayOf("west", "east")

                // Corners
                for ((nsIndex, connectionNS) in ns.withIndex()) {
                    for ((weIndex, connectionWE) in we.withIndex()) {
                        yield(
                            Multipart(
                                `when` = When(mapOf(
                                    connectionNS to "true",
                                    connectionWE to "true",
                                    ns[1 - nsIndex] to "false",
                                    we[1 - weIndex] to "false"
                                )),
                                apply = Variant(
                                    model = id.wrapPath("block/", "_table_leg"),
                                    y = getCornerLegRotation(connectionNS, connectionWE),
                                    uvlock = true
                                )
                            )
                        )
                    }
                }
            }.toList()
        ).suffixed("table")
    )

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
}


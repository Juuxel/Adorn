package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.data.BlockStateProperty
import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.ContentGenerator
import io.github.cottonmc.jsonfactory.output.model.ModelBlockState.Variant
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState.Multipart
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState.When
import io.github.cottonmc.jsonfactory.output.suffixed

object KitchenCounterBlockState : ContentGenerator("Kitchen Counter Block State", "blockstates", AdornPlugin.KITCHEN) {
    override fun generate(id: Identifier) = listOf(
        /*ModelBlockState.create(id, setOf(BlockStateProperty.horizontalFacing)) { values, variant ->
            variant.copy(
                model = variant.model.suffixPath("_kitchen_counter"),
                y = getYRotation(values["facing"] ?: "")
            )
        }.suffixed("kitchen_counter")*/
        MultipartBlockState(
            multipart = BlockStateProperty.horizontalFacing.values.flatMap { facing ->
                listOf(
                    Multipart(
                        `when` = When("facing", facing),
                        apply = Variant(model = id.wrapPath("block/", "_kitchen_counter"), y = getYRotation(facing))
                    ),
                    Multipart(
                        `when` = When(mapOf("facing" to facing, "front" to "left")),
                        apply = Variant(model = id.wrapPath("block/", "_kitchen_counter_connection_left"), y = getYRotation(facing))
                    ),
                    Multipart(
                        `when` = When(mapOf("facing" to facing, "front" to "right")),
                        apply = Variant(model = id.wrapPath("block/", "_kitchen_counter_connection_right"), y = getYRotation(facing))
                    )
                )
            }
        ).suffixed("kitchen_counter")
    )

    private fun getYRotation(facing: String): Int = when (facing) {
        "east" -> 0
        "south" -> 90
        "west" -> 180
        "north" -> 270

        else -> 0
    }
}

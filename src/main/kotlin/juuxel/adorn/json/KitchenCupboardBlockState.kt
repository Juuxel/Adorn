package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.data.BlockStateProperty
import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.ContentGenerator
import io.github.cottonmc.jsonfactory.output.model.ModelBlockState.Variant
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState.*
import io.github.cottonmc.jsonfactory.output.suffixed

object KitchenCupboardBlockState : ContentGenerator("Kitchen Cupboard Block State", "blockstates", AdornPlugin.KITCHEN) {
    override fun generate(id: Identifier) = listOf(
        MultipartBlockState(
            multipart = BlockStateProperty.horizontalFacing.values.flatMap {
                listOf(
                    Multipart(
                        `when` = When("facing", it),
                        apply = Variant(
                            model = id.wrapPath("block/", "_kitchen_counter"),
                            y = getYRotation(it),
                            uvlock = true
                        )
                    ),
                    Multipart(
                        `when` = When("facing", it),
                        apply = Variant(
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

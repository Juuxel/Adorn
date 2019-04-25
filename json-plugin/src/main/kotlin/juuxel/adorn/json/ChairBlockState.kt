package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.data.BlockStateProperty
import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.ContentGenerator
import io.github.cottonmc.jsonfactory.output.model.ModelBlockState
import io.github.cottonmc.jsonfactory.output.suffixed

object ChairBlockState : ContentGenerator("Chair Block State", "blockstates", AdornPlugin.CHAIR) {
    override fun generate(id: Identifier) = listOf(
        ModelBlockState.create(id, setOf(BlockStateProperty.horizontalFacing)) { values, variant ->
            variant.copy(
                model = variant.model.suffixPath("_chair"),
                y = getYRotation(values["facing"] ?: "")
            )
        }.suffixed("chair")
    )

    private fun getYRotation(facing: String): Int = when (facing) {
        "east" -> 0
        "south" -> 90
        "west" -> 180
        "north" -> 270

        else -> 0
    }
}

package juuxel.adorn.json.shelf

import io.github.cottonmc.jsonfactory.data.BlockStateProperty
import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.output.model.VariantBlockState
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin

object ShelfBlockState : AbstractContentGenerator("shelf.block_state", "blockstates", AdornPlugin.SHELF) {
    override fun generate(id: Identifier) = listOf(
        VariantBlockState.create(id, setOf(BlockStateProperty.horizontalFacing)) { values, variant ->
            variant.copy(
                model = variant.model.suffixPath("_shelf"),
                y = getYRotation(values["facing"] ?: "")
            )
        }.suffixed("shelf")
    )

    private fun getYRotation(facing: String): Int = when (facing) {
        "east" -> 0
        "south" -> 90
        "west" -> 180
        "north" -> 270

        else -> 0
    }
}

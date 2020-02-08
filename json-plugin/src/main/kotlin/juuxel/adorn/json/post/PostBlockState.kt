package juuxel.adorn.json.post

import io.github.cottonmc.jsonfactory.data.BlockStateProperty
import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.output.Output
import io.github.cottonmc.jsonfactory.output.model.VariantBlockState
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin

object PostBlockState : AbstractContentGenerator(
    "post.block_state", "blockstates", AdornPlugin.POST
) {
    override fun generate(id: Identifier): List<Output> = listOf(
        VariantBlockState.create(id, setOf(BlockStateProperty.axis)) { values, variant ->
            when (values[BlockStateProperty.axis]) {
                "x" -> variant.copy(model = id.suffixPath("_post"), x = 90, y = 90)
                "z" -> variant.copy(model = id.suffixPath("_post"), x = 90)
                else -> variant.copy(model = id.suffixPath("_post"))
            }
        }.suffixed("post")
    )
}

package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.gens.GeneratorInfo
import io.github.cottonmc.jsonfactory.gens.ResourceRoot
import io.github.cottonmc.jsonfactory.output.ExperimentalMapOutput
import io.github.cottonmc.jsonfactory.output.MapJsonOutput
import io.github.cottonmc.jsonfactory.output.suffixed

class SuffixedStonecuttingRecipe(
    id: String,
    info: GeneratorInfo,
    private val suffix: String,
    private val count: Int,
    private val pathSuffix: String = ""
) : AbstractContentGenerator(id, "recipes/stonecutting", info, resourceRoot = ResourceRoot.Data) {
    @ExperimentalMapOutput
    override fun generate(id: Identifier) = listOf(
        MapJsonOutput(
            mapOf(
                "type" to "stonecutting",
                "ingredient" to mapOf(
                    "item" to Identifier.mc(id.path + pathSuffix)
                ),
                "result" to id.suffixPath("_$suffix"),
                "count" to count
            )
        ).suffixed(suffix)
    )
}

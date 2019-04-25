package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.ContentGenerator
import io.github.cottonmc.jsonfactory.gens.GeneratorInfo
import io.github.cottonmc.jsonfactory.output.ExperimentalMapOutput
import io.github.cottonmc.jsonfactory.output.MapJsonOutput
import io.github.cottonmc.jsonfactory.output.suffixed

class RecipeAdvancementGenerator(
    name: String,
    info: GeneratorInfo,
    private val suffix: String,
    private val keyItems: List<(Identifier) -> Identifier>
) :
    ContentGenerator(name, "advancements/recipes", info, resourceRoot = ResourceRoot.Data) {
    @ExperimentalMapOutput
    override fun generate(id: Identifier) = listOf(
        MapJsonOutput(
            mapOf(
                "parent" to "minecraft:recipes/root",
                "rewards" to mapOf(
                    "recipes" to listOf(id.suffixPath("_$suffix"))
                ),
                "criteria" to (
                    keyItems.mapIndexed { i, itemFn ->
                        "has_key_item_$i" to mapOf(
                            "trigger" to "minecraft:inventory_changed",
                            "conditions" to mapOf(
                                "items" to listOf(
                                    mapOf("item" to itemFn(id))
                                )
                            )
                        )
                    } + ("has_the_recipe" to mapOf(
                        "trigger" to "minecraft:recipe_unlocked",
                        "conditions" to mapOf(
                            "recipe" to id.suffixPath("_$suffix")
                        )
                    ))
                ).toMap(),
                "requirements" to listOf(
                    keyItems.indices.map { "has_key_item_$it" } + "has_the_recipe"
                )
            )
        ).suffixed(suffix)
    )
}

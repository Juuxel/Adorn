package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.gens.GeneratorInfo
import io.github.cottonmc.jsonfactory.gens.ResourceRoot
import io.github.cottonmc.jsonfactory.output.ExperimentalMapOutput
import io.github.cottonmc.jsonfactory.output.MapJsonOutput

class RecipeAdvancementGenerator(
    id: String,
    info: GeneratorInfo,
    private val keyItems: List<(Identifier) -> Identifier>
) : AbstractContentGenerator(id, "advancements/recipes", info, resourceRoot = ResourceRoot.Data) {
    @ExperimentalMapOutput
    override fun generate(id: Identifier) = listOf(
        MapJsonOutput(
            mapOf(
                "parent" to "minecraft:recipes/root",
                "rewards" to mapOf(
                    "recipes" to listOf(id)
                ),
                "criteria" to (
                    keyItems.mapIndexed { i, itemFn ->
                        "has_key_item_$i" to mapOf(
                            "trigger" to "minecraft:inventory_changed",
                            "conditions" to mapOf(
                                "items" to listOf(
                                    mapOf("items" to listOf(itemFn(id)))
                                )
                            )
                        )
                    } + ("has_the_recipe" to mapOf(
                        "trigger" to "minecraft:recipe_unlocked",
                        "conditions" to mapOf(
                            "recipe" to id
                        )
                    ))
                ).toMap(),
                "requirements" to listOf(
                    keyItems.indices.map { "has_key_item_$it" } + "has_the_recipe"
                )
            )
        )
    )
}

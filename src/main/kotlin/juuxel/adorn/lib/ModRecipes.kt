package juuxel.adorn.lib

import io.github.juuxel.polyester.registry.PolyesterRegistry
import juuxel.adorn.Adorn
import juuxel.adorn.recipe.DeprecationReplacementRecipe
import net.minecraft.util.registry.Registry

object ModRecipes : PolyesterRegistry(Adorn.NAMESPACE) {
    val DEPRECATION_REPLACEMENT_SERIALIZER = register(
        Registry.RECIPE_SERIALIZER,
        "deprecation_replacement",
        DeprecationReplacementRecipe.Serializer()
    )

    fun init() {}
}

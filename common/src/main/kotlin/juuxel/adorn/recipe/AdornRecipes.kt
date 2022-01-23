package juuxel.adorn.recipe

import juuxel.adorn.AdornCommon
import juuxel.adorn.platform.PlatformBridges
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType

object AdornRecipes {
    val RECIPE_SERIALIZERS = PlatformBridges.registrarFactory.recipeSerializer()
    val BREWING_TYPE: RecipeType<BrewingRecipe> = RecipeType.register(AdornCommon.id("brewing").toString())
    val BREWING_SERIALIZER: RecipeSerializer<BrewingRecipe> by RECIPE_SERIALIZERS.register("brewing") { BrewingRecipe.Serializer() }

    fun init() {
    }
}

package juuxel.adorn.recipe

import juuxel.adorn.AdornCommon
import juuxel.adorn.platform.PlatformBridges
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType

object AdornRecipes {
    val RECIPE_SERIALIZERS = PlatformBridges.registrarFactory.recipeSerializer()
    val BREWING_TYPE: RecipeType<IBrewingRecipe> = RecipeType.register(AdornCommon.id("brewing").toString())
    val BREWING_SERIALIZER: RecipeSerializer<BrewingRecipe> by RECIPE_SERIALIZERS.register("brewing") { BrewingRecipe.Serializer() }
    val BREWING_FROM_FLUID_SERIALIZER: RecipeSerializer<FluidBrewingRecipe> by RECIPE_SERIALIZERS.register("brewing_from_fluid") {
        FluidBrewingRecipe.Serializer()
    }

    fun init() {
    }
}

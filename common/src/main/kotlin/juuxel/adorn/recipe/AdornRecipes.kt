package juuxel.adorn.recipe

import juuxel.adorn.AdornCommon
import juuxel.adorn.lib.registry.Registered
import juuxel.adorn.lib.registry.RegistrarFactory
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.SpecialRecipeSerializer
import net.minecraft.registry.RegistryKeys

object AdornRecipes {
    val RECIPE_SERIALIZERS = RegistrarFactory.get().create(RegistryKeys.RECIPE_SERIALIZER)
    val RECIPE_TYPES = RegistrarFactory.get().create(RegistryKeys.RECIPE_TYPE)

    val BREWING_TYPE: RecipeType<BrewingRecipe> by registerRecipeType("brewing")
    val BREWING_SERIALIZER: RecipeSerializer<ItemBrewingRecipe> by RECIPE_SERIALIZERS.register("brewing") { ItemBrewingRecipe.Serializer() }
    val BREWING_FROM_FLUID_SERIALIZER: RecipeSerializer<FluidBrewingRecipe> by RECIPE_SERIALIZERS.register("brewing_from_fluid") {
        FluidBrewingRecipe.Serializer()
    }

    val FERTILIZER_REFILLING_TYPE: RecipeType<FertilizerRefillingRecipe> by registerRecipeType("fertilizer_refilling")
    val FERTILIZER_REFILLING_SERIALIZER: RecipeSerializer<FertilizerRefillingRecipe> by RECIPE_SERIALIZERS.register("fertilizer_refilling") {
        SpecialRecipeSerializer(::FertilizerRefillingRecipe)
    }

    fun init() {
    }

    private fun <R : Recipe<*>> registerRecipeType(id: String): Registered<RecipeType<R>> =
        RECIPE_TYPES.register(id) {
            object : RecipeType<R> {
                override fun toString(): String = "${AdornCommon.NAMESPACE}:$id"
            }
        }
}

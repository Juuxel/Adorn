package juuxel.adorn.recipe

import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeType

interface BrewingRecipe : Recipe<BrewerInventory> {
    override fun getType(): RecipeType<*> =
        AdornRecipes.BREWING_TYPE
}

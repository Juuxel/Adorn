package juuxel.adorn.recipe

import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeType

// TODO: Naming
interface IBrewingRecipe : Recipe<BrewerInventory> {
    override fun getType(): RecipeType<*> =
        AdornRecipes.BREWING_TYPE
}

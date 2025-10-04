package juuxel.adorn.recipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;

public sealed interface BrewingRecipe extends Recipe<BrewerInput> permits FluidBrewingRecipe, ItemBrewingRecipe {
    @Override
    default RecipeType<BrewingRecipe> getType() {
        return AdornRecipeTypes.BREWING.get();
    }

    @Override
    default RecipeBookCategory getRecipeBookCategory() {
        return AdornRecipeBookCategories.BREWING.get();
    }
}

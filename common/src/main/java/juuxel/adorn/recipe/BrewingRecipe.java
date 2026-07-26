package juuxel.adorn.recipe;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.RecipeBookCategory;

public sealed interface BrewingRecipe extends Recipe<BrewerInput> permits FluidBrewingRecipe, ItemBrewingRecipe {
    @Override
    default RecipeType<BrewingRecipe> getType() {
        return AdornRecipeTypes.BREWING.get();
    }

    @Override
    default RecipeBookCategory recipeBookCategory() {
        return AdornRecipeBookCategories.BREWING.get();
    }
}

package juuxel.adorn.compat.jei;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.recipe.BrewingRecipe;
import mezz.jei.api.recipe.RecipeType;

public final class JeiRecipeTypes {
    public static final RecipeType<BrewingRecipe> BREWER = RecipeType.create(AdornCommon.NAMESPACE, "brewer", BrewingRecipe.class);
}

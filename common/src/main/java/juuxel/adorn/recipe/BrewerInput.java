package juuxel.adorn.recipe;

import juuxel.adorn.fluid.FluidReference;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.Optional;

public interface BrewerInput extends RecipeInput {
    FluidReference getFluidReference();

    default boolean matches(int slot, Ingredient ingredient) {
        return ingredient.test(getItem(slot));
    }

    default boolean matches(int slot, Optional<Ingredient> ingredient) {
        return Ingredient.testOptionalIngredient(ingredient, getItem(slot));
    }
}

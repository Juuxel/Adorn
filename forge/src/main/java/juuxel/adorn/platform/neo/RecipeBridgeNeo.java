package juuxel.adorn.platform.neo;

import juuxel.adorn.fluid.FluidIngredient;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.platform.RecipeBridge;
import net.minecraft.recipe.display.DisplayedItemFactory;
import net.minecraft.util.context.ContextParameterMap;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.display.ForFluidStacks;

import java.util.stream.Stream;

public final class RecipeBridgeNeo implements RecipeBridge {
    @Override
    public <T> Stream<T> appendFluidIngredientStacks(FluidIngredient ingredient, ContextParameterMap parameters, DisplayedItemFactory<T> factory) {
        if (factory instanceof ForFluidStacks<T> forFluidStacks) {
            return ingredient.fluid()
                .getFluids()
                .stream()
                .map(fluid -> new FluidStack(
                    fluid.getRegistryEntry(),
                    (int) FluidUnit.convert(ingredient.amount(), ingredient.unit(), FluidUnit.LITRE),
                    ingredient.components()
                ))
                .map(forFluidStacks::forStack);
        }

        return Stream.empty();
    }
}

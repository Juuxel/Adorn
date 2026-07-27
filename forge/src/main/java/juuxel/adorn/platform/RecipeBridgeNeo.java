package juuxel.adorn.platform;

import juuxel.adorn.fluid.FluidIngredient;
import juuxel.adorn.fluid.FluidUnit;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.crafting.display.DisplayContentsFactory;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.display.ForFluidStacks;

import java.util.stream.Stream;

public final class RecipeBridgeNeo implements RecipeBridge {
    @Override
    public <T> Stream<T> appendFluidIngredientStacks(FluidIngredient ingredient, ContextMap parameters, DisplayContentsFactory<T> factory) {
        if (factory instanceof ForFluidStacks<T> forFluidStacks) {
            return ingredient.fluid()
                .getFluids()
                .stream()
                .map(fluid -> new FluidStack(
                    fluid.builtInRegistryHolder(),
                    (int) FluidUnit.convert(ingredient.amount(), ingredient.unit(), FluidUnit.LITRE),
                    ingredient.components()
                ))
                .map(forFluidStacks::forStack);
        }

        return Stream.empty();
    }
}

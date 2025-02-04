package juuxel.adorn.recipe;

import juuxel.adorn.fluid.FluidIngredient;
import juuxel.adorn.platform.RecipeBridge;
import net.minecraft.recipe.display.DisplayedItemFactory;
import net.minecraft.util.context.ContextParameterMap;

import java.util.stream.Stream;

public final class RecipeBridgeFabric implements RecipeBridge {
    @Override
    public <T> Stream<T> appendFluidIngredientStacks(FluidIngredient ingredient, ContextParameterMap parameters, DisplayedItemFactory<T> factory) {
        return Stream.empty();
    }
}

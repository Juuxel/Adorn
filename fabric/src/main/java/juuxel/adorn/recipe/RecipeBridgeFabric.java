package juuxel.adorn.recipe;

import juuxel.adorn.fluid.FluidIngredient;
import juuxel.adorn.platform.RecipeBridge;
import net.minecraft.world.item.crafting.display.DisplayContentsFactory;
import net.minecraft.util.context.ContextMap;

import java.util.stream.Stream;

public final class RecipeBridgeFabric implements RecipeBridge {
    @Override
    public <T> Stream<T> appendFluidIngredientStacks(FluidIngredient ingredient, ContextMap parameters, DisplayContentsFactory<T> factory) {
        return Stream.empty();
    }
}

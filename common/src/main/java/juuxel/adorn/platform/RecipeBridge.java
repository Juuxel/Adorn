package juuxel.adorn.platform;

import juuxel.adorn.fluid.FluidIngredient;
import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;
import net.minecraft.recipe.display.DisplayedItemFactory;
import net.minecraft.util.context.ContextParameterMap;

import java.util.stream.Stream;

public interface RecipeBridge {
    <T> Stream<T> appendFluidIngredientStacks(FluidIngredient ingredient, ContextParameterMap parameters, DisplayedItemFactory<T> factory);

    @InlineServices.Getter
    static RecipeBridge get() {
        return Services.load(RecipeBridge.class);
    }
}

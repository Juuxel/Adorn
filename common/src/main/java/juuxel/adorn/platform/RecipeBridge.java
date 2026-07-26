package juuxel.adorn.platform;

import juuxel.adorn.fluid.FluidIngredient;
import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;
import net.minecraft.world.item.crafting.display.DisplayContentsFactory;
import net.minecraft.util.context.ContextMap;

import java.util.stream.Stream;

public interface RecipeBridge {
    <T> Stream<T> appendFluidIngredientStacks(FluidIngredient ingredient, ContextMap parameters, DisplayContentsFactory<T> factory);

    @InlineServices.Getter
    static RecipeBridge get() {
        return Services.load(RecipeBridge.class);
    }
}

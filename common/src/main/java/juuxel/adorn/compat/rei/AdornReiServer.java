package juuxel.adorn.compat.rei;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.recipe.AdornRecipeTypes;
import juuxel.adorn.recipe.FluidBrewingRecipe;
import juuxel.adorn.recipe.ItemBrewingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.api.common.registry.display.ServerDisplayRegistry;

public class AdornReiServer implements REICommonPlugin {
    public static final CategoryIdentifier<BrewerDisplay> BREWER = CategoryIdentifier.of(AdornCommon.id("brewer"));

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(BREWER.getIdentifier(), BrewerDisplay.SERIALIZER);
    }

    @Override
    public void registerDisplays(ServerDisplayRegistry registry) {
        registry.beginRecipeFiller(ItemBrewingRecipe.class)
            .filterType(AdornRecipeTypes.BREWING.get())
            .fill(entry -> new BrewerDisplay(entry.value(), entry.id().getValue()));
        registry.beginRecipeFiller(FluidBrewingRecipe.class)
            .filterType(AdornRecipeTypes.BREWING.get())
            .fill(entry -> new BrewerDisplay(entry.value(), entry.id().getValue()));
    }
}

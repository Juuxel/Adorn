package juuxel.adorn.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.client.gui.screen.TradingStationScreen;
import juuxel.adorn.recipe.AdornRecipeTypes;
import juuxel.adorn.recipe.FluidBrewingRecipe;
import juuxel.adorn.recipe.ItemBrewingRecipe;

@EmiEntrypoint
public final class AdornEmiPlugin implements EmiPlugin {
    public static final EmiRecipeCategory BREWER_CATEGORY = new EmiRecipeCategory(
        AdornCommon.id("brewer"),
        EmiStack.of(AdornBlocks.BREWER.get()),
        new EmiTexture(AdornCommon.id("textures/gui/recipe_viewer/brewer_light.png"), 240, 0, 16, 16)
    );

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(BREWER_CATEGORY);
        registry.addWorkstation(BREWER_CATEGORY, EmiStack.of(AdornBlocks.BREWER.get()));

        var recipeManager = registry.getRecipeManager();

        for (var entry : recipeManager.listAllOfType(AdornRecipeTypes.BREWING.get())) {
            BrewingEmiRecipe emiRecipe = switch (entry.value()) {
                case ItemBrewingRecipe recipe -> new BrewingEmiRecipe(entry.id(), recipe);
                case FluidBrewingRecipe recipe -> new BrewingEmiRecipe(entry.id(), recipe);
            };

            registry.addRecipe(emiRecipe);
        }

        registry.addDragDropHandler(TradingStationScreen.class, new TradingStationDragDropHandler());
    }
}

package juuxel.adorn.client.gui.widget;

import com.google.common.base.Suppliers;
import juuxel.adorn.AdornCommon;
import juuxel.adorn.item.AdornItems;
import juuxel.adorn.menu.BrewerMenu;
import juuxel.adorn.recipe.AdornRecipeBookCategories;
import juuxel.adorn.recipe.BrewingRecipeDisplay;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.recipebook.GhostRecipe;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.menu.slot.Slot;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.text.Text;
import net.minecraft.util.context.ContextParameterMap;

import java.util.List;
import java.util.function.Supplier;

public final class BrewingRecipeBookWidget extends RecipeBookWidget<BrewerMenu> {
    // Supplier to avoid errors due to unregistered content
    private static final Supplier<List<Tab>> TABS = Suppliers.memoize(() -> List.of(new Tab(AdornItems.MUG.get(), AdornRecipeBookCategories.BREWING.get())));
    private static final ButtonTextures FILTER_BUTTON_TEXTURES = new ButtonTextures(
        AdornCommon.id("recipe_book/brewer_filter_enabled"),
        AdornCommon.id("recipe_book/brewer_filter_disabled"),
        AdornCommon.id("recipe_book/brewer_filter_enabled_highlighted"),
        AdornCommon.id("recipe_book/brewer_filter_disabled_highlighted")
    );
    private static final Text TOGGLE_BREWABLE_TEXT = Text.translatable("gui.adorn.recipebook.toggleRecipes.brewable");

    public BrewingRecipeBookWidget(BrewerMenu menu) {
        super(menu, TABS.get());
    }

    @Override
    protected void setBookButtonTexture() {
        toggleCraftableButton.setTextures(FILTER_BUTTON_TEXTURES);
    }

    @Override
    protected boolean isValid(Slot slot) {
        return switch (slot.id) {
            case 0, 1, 2 -> true;
            default -> false;
        };
    }

    @Override
    protected void populateRecipes(RecipeResultCollection recipeResultCollection, RecipeFinder recipeFinder) {
        recipeResultCollection.populateRecipes(recipeFinder, display -> display instanceof BrewingRecipeDisplay);
    }

    @Override
    protected Text getToggleCraftableButtonText() {
        return TOGGLE_BREWABLE_TEXT;
    }

    @Override
    protected void showGhostRecipe(GhostRecipe ghostRecipe, RecipeDisplay display, ContextParameterMap context) {
        if (display instanceof BrewingRecipeDisplay brewing) {
            // Use addResults to get the correct format
            ghostRecipe.addResults(craftingMenu.getMainSlot(), context, brewing.input());
            ghostRecipe.addInputs(craftingMenu.getFirstIngredientSlot(), context, brewing.firstIngredient());
            ghostRecipe.addInputs(craftingMenu.getSecondIngredientSlot(), context, brewing.secondIngredient());
        }
    }
}

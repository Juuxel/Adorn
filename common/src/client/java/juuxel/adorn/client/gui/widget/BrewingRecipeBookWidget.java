package juuxel.adorn.client.gui.widget;

import com.google.common.base.Suppliers;
import juuxel.adorn.AdornCommon;
import juuxel.adorn.item.AdornItems;
import juuxel.adorn.menu.BrewerMenu;
import juuxel.adorn.recipe.AdornRecipeBookCategories;
import juuxel.adorn.recipe.BrewingRecipeDisplay;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.network.chat.Component;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.display.RecipeDisplay;

import java.util.List;
import java.util.function.Supplier;

public final class BrewingRecipeBookWidget extends RecipeBookComponent<BrewerMenu> {
    // Supplier to avoid errors due to unregistered content
    private static final Supplier<List<TabInfo>> TABS = Suppliers.memoize(() -> List.of(new TabInfo(AdornItems.MUG.get(), AdornRecipeBookCategories.BREWING.get())));
    private static final WidgetSprites FILTER_BUTTON_TEXTURES = new WidgetSprites(
        AdornCommon.id("recipe_book/brewer_filter_enabled"),
        AdornCommon.id("recipe_book/brewer_filter_disabled"),
        AdornCommon.id("recipe_book/brewer_filter_enabled_highlighted"),
        AdornCommon.id("recipe_book/brewer_filter_disabled_highlighted")
    );
    private static final Component TOGGLE_BREWABLE_TEXT = Component.translatable("gui.adorn.recipebook.toggleRecipes.brewable");

    public BrewingRecipeBookWidget(BrewerMenu menu) {
        super(menu, TABS.get());
    }

    @Override
    protected WidgetSprites getFilterButtonTextures() {
        return FILTER_BUTTON_TEXTURES;
    }

    @Override
    protected boolean isCraftingSlot(Slot slot) {
        return switch (slot.index) {
            case 0, 1, 2 -> true;
            default -> false;
        };
    }

    @Override
    protected void selectMatchingRecipes(RecipeCollection recipeResultCollection, StackedItemContents recipeFinder) {
        recipeResultCollection.selectRecipes(recipeFinder, display -> display instanceof BrewingRecipeDisplay);
    }

    @Override
    protected Component getRecipeFilterName() {
        return TOGGLE_BREWABLE_TEXT;
    }

    @Override
    protected void fillGhostRecipe(GhostSlots ghostRecipe, RecipeDisplay display, ContextMap context) {
        if (display instanceof BrewingRecipeDisplay brewing) {
            // Use addResults to get the correct format
            ghostRecipe.setResult(menu.getMainSlot(), context, brewing.input());
            ghostRecipe.setInput(menu.getFirstIngredientSlot(), context, brewing.firstIngredient());
            ghostRecipe.setInput(menu.getSecondIngredientSlot(), context, brewing.secondIngredient());
        }
    }
}

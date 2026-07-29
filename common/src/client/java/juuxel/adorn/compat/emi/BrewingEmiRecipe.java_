package juuxel.adorn.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TankWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.entity.BrewerBlockEntity;
import juuxel.adorn.client.gui.screen.BrewerScreen;
import juuxel.adorn.item.AdornItems;
import juuxel.adorn.platform.FluidBridge;
import juuxel.adorn.recipe.FluidBrewingRecipe;
import juuxel.adorn.recipe.ItemBrewingRecipe;
import net.minecraft.util.Identifier;

import java.util.List;

public record BrewingEmiRecipe(
    Identifier id,
    EmiIngredient inputItem,
    EmiIngredient firstItemIngredient,
    EmiIngredient secondItemIngredient,
    EmiIngredient fluidIngredient,
    EmiStack result
) implements EmiRecipe {
    private static final int PADDING = 0;
    private static final int FLUID_SCALE_Z_OFFSET = 100;
    private static final Identifier TEXTURE = AdornCommon.id("textures/gui/recipe_viewer/brewer_light.png");

    public BrewingEmiRecipe(Identifier id, ItemBrewingRecipe recipe) {
        this(
            id,
            EmiStack.of(AdornItems.MUG.get()),
            EmiUtil.withRemainders(EmiIngredient.of(recipe.firstIngredient())),
            EmiUtil.withRemainders(EmiIngredient.of(recipe.secondIngredient())),
            EmiStack.EMPTY,
            EmiStack.of(recipe.result())
        );
    }

    public BrewingEmiRecipe(Identifier id, FluidBrewingRecipe recipe) {
        this(
            id,
            EmiStack.of(AdornItems.MUG.get()),
            EmiUtil.withRemainders(EmiIngredient.of(recipe.firstIngredient())),
            EmiUtil.withRemainders(EmiIngredient.of(recipe.secondIngredient())),
            EmiUtil.emiIngredientOf(recipe.fluid()),
            EmiStack.of(recipe.result())
        );
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return AdornEmiPlugin.BREWER_CATEGORY;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(inputItem, firstItemIngredient, secondItemIngredient, fluidIngredient);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(result);
    }

    @Override
    public int getDisplayWidth() {
        return 78 + 27 + 2 * PADDING;
    }

    @Override
    public int getDisplayHeight() {
        return 61 + 2 * PADDING;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        int leftX = PADDING;
        int topY = PADDING;
        widgets.addTexture(TEXTURE, leftX, topY, 105, 61, 49, 16);
        widgets.addSlot(firstItemIngredient, leftX, topY).drawBack(false);
        widgets.addSlot(secondItemIngredient, leftX + 60, topY).drawBack(false);
        widgets.addSlot(result, leftX + 26, topY + 35).drawBack(false).recipeContext(this).large(true);
        var capacity = BrewerBlockEntity.FLUID_CAPACITY_IN_BUCKETS * FluidBridge.get().getFluidUnit().getBucketVolume();
        widgets.add(new TankWidget(fluidIngredient, leftX + 87, topY, 18, BrewerScreen.FLUID_AREA_HEIGHT + 2, capacity).drawBack(false));

        // Empty mug
        widgets.addSlot(inputItem, leftX + 3, topY + 38).drawBack(false);

        // Fluid scale
        widgets.addDrawable(leftX + 88, topY + 1, 16, BrewerScreen.FLUID_AREA_HEIGHT, (context, mouseX, mouseY, tickDelta) -> {
            context.drawTexture(TEXTURE, 0, 0, FLUID_SCALE_Z_OFFSET, 154f, 17f, 16, BrewerScreen.FLUID_AREA_HEIGHT, 256, 256);
        });

        // Progress arrow
        widgets.addDrawable(leftX + 35, topY + 8, 8, BrewerScreen.FLUID_AREA_HEIGHT, (context, mouseX, mouseY, tickDelta) -> {
            float progressFraction = (System.currentTimeMillis() % 4000) / 4000f;
            int height = Math.round(progressFraction * 25);
            context.drawTexture(TEXTURE, 0, 0, 1, 176f, 0f, 8, height, 256, 256);
        });
    }
}

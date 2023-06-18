package juuxel.adorn.compat.jei;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.block.entity.BrewerBlockEntity;
import juuxel.adorn.client.gui.screen.BrewerScreen;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.item.AdornItems;
import juuxel.adorn.platform.FluidBridge;
import juuxel.adorn.recipe.BrewingRecipe;
import juuxel.adorn.recipe.FluidBrewingRecipe;
import juuxel.adorn.recipe.ItemBrewingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.fluid.Fluid;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class BrewerCategory implements IRecipeCategory<BrewingRecipe> {
    private static final Identifier TEXTURE = AdornCommon.id("textures/gui/recipe_viewer/brewer_light.png");
    private final IDrawable background = new Background();
    private final IDrawable icon;
    private final IGuiHelper guiHelper;

    public BrewerCategory(IJeiHelpers helpers) {
        this.guiHelper = helpers.getGuiHelper();
        icon = guiHelper.createDrawableItemStack(AdornBlocks.INSTANCE.getBREWER().asItem().getDefaultStack());
    }

    @Override
    public RecipeType<BrewingRecipe> getRecipeType() {
        return JeiRecipeTypes.BREWER;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("category.adorn.brewer"); // same as REI
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder layoutBuilder, BrewingRecipe recipe, IFocusGroup focusGroup) {
        var leftX = 0;
        var topY = 0;
        var firstSlot = layoutBuilder.addSlot(RecipeIngredientRole.INPUT, leftX + 1, topY + 1);
        var secondSlot = layoutBuilder.addSlot(RecipeIngredientRole.INPUT, leftX + 61, topY + 1);
        var resultSlot = layoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, leftX + 31, topY + 40);
        layoutBuilder.addSlot(RecipeIngredientRole.INPUT, leftX + 4, topY + 39)
            .addIngredients(Ingredient.ofItems(AdornItems.INSTANCE.getMUG()));
        var capacity = FluidBridge.get().getFluidUnit().getBucketVolume() * BrewerBlockEntity.FLUID_CAPACITY_IN_BUCKETS;
        var tank = layoutBuilder.addSlot(RecipeIngredientRole.INPUT, leftX + 88, topY + 1)
            .setFluidRenderer(capacity, false, 16, BrewerScreen.FLUID_AREA_HEIGHT)
            .setOverlay(guiHelper.createDrawable(TEXTURE, 154, 17, 16, BrewerScreen.FLUID_AREA_HEIGHT), 0, 0);

        if (recipe instanceof ItemBrewingRecipe r) {
            firstSlot.addIngredients(r.getFirstIngredient());
            secondSlot.addIngredients(r.getSecondIngredient());
            resultSlot.addItemStack(r.getResult());
        } else if (recipe instanceof FluidBrewingRecipe r) {
            firstSlot.addIngredients(r.getFirstIngredient());
            secondSlot.addIngredients(r.getSecondIngredient());
            resultSlot.addItemStack(r.getResult());

            var ingredient = r.getFluid();
            var amount = FluidUnit.convert(ingredient.getAmount(), ingredient.getUnit(), FluidBridge.get().getFluidUnit());
            for (Fluid fluid : ingredient.getFluid().getFluids()) {
                tank.addFluidStack(fluid, amount, ingredient.getNbt());
            }
        }
    }

    private static final class Background implements IDrawable {
        @Override
        public int getWidth() {
            return 105;
        }

        @Override
        public int getHeight() {
            return 61;
        }

        @Override
        public void draw(DrawContext context, int offsetX, int offsetY) {
            context.drawTexture(TEXTURE, offsetX, offsetY, 49, 16, getWidth(), getHeight());
            float progressFraction = (System.currentTimeMillis() % 4000) / 4000f;
            int height = Math.round(progressFraction * 25);
            context.drawTexture(TEXTURE, offsetX + 35, offsetY + 8, 176, 0, 8, height);
        }
    }
}

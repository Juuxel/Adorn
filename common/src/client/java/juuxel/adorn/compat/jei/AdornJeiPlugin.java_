package juuxel.adorn.compat.jei;

import com.google.common.collect.Lists;
import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.client.gui.screen.TradingStationScreen;
import juuxel.adorn.recipe.AdornRecipeTypes;
import me.shedaniel.rei.plugincompatibilities.api.REIPluginCompatIgnore;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.MinecraftClient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.util.Identifier;

@JeiPlugin
@REIPluginCompatIgnore
public final class AdornJeiPlugin implements IModPlugin {
    private static final Identifier ID = AdornCommon.id("jei_plugin");

    @Override
    public Identifier getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new BrewerCategory(registration.getJeiHelpers()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registerRecipes(registration, AdornRecipeTypes.BREWING.get(), JeiRecipeTypes.BREWER);
    }

    private <I extends RecipeInput, T extends Recipe<I>> void registerRecipes(
        IRecipeRegistration registration, RecipeType<T> type, mezz.jei.api.recipe.RecipeType<T> jeiType) {
        RecipeManager manager = MinecraftClient.getInstance().world.getRecipeManager();
        registration.addRecipes(jeiType, Lists.transform(manager.listAllOfType(type), RecipeEntry::value));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(AdornBlocks.BREWER.get().asItem().getDefaultStack(), JeiRecipeTypes.BREWER);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGhostIngredientHandler(TradingStationScreen.class, new TradingStationGhostIngredientHandler());
    }
}

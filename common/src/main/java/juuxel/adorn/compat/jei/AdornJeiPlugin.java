package juuxel.adorn.compat.jei;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.recipe.AdornRecipes;
import me.shedaniel.rei.plugincompatibilities.api.REIPluginCompatIgnore;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
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
        registerRecipes(registration, AdornRecipes.INSTANCE.getBREWING_TYPE(), JeiRecipeTypes.BREWER);
    }

    private <C extends Inventory, T extends Recipe<C>> void registerRecipes(
        IRecipeRegistration registration, RecipeType<T> type, mezz.jei.api.recipe.RecipeType<T> jeiType) {
        RecipeManager manager = MinecraftClient.getInstance().world.getRecipeManager();
        registration.addRecipes(jeiType, manager.listAllOfType(type));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(AdornBlocks.INSTANCE.getBREWER().asItem().getDefaultStack(), JeiRecipeTypes.BREWER);
    }
}

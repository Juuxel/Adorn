package juuxel.adorn.recipe;

import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.core.registries.Registries;

public final class AdornRecipeBookCategories {
    public static final Registrar<RecipeBookCategory> RECIPE_BOOK_CATEGORIES = RegistrarFactory.get().create(Registries.RECIPE_BOOK_CATEGORY);

    public static final Registered<RecipeBookCategory> BREWING = RECIPE_BOOK_CATEGORIES.register("brewing", RecipeBookCategory::new);

    public static void init() {
    }
}

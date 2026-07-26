package juuxel.adorn.recipe;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.core.registries.Registries;

public final class AdornRecipeTypes {
    public static final Registrar<RecipeType<?>> RECIPE_TYPES = RegistrarFactory.get().create(Registries.RECIPE_TYPE);

    public static final Registered<RecipeType<BrewingRecipe>> BREWING = registerRecipeType("brewing");

    private static <R extends Recipe<?>> Registered<RecipeType<R>> registerRecipeType(String id) {
        return RECIPE_TYPES.register(id, () -> new RecipeType<>() {
            @Override
            public String toString() {
                return AdornCommon.NAMESPACE + ':' + id;
            }
        });
    }

    public static void init() {
    }
}

package juuxel.adorn.recipe;

import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.registry.RegistryKeys;

public final class AdornRecipeDisplays {
    public static final Registrar<RecipeDisplay.Serializer<?>> RECIPE_DISPLAYS = RegistrarFactory.get().create(RegistryKeys.RECIPE_DISPLAY);

    public static void init() {
        RECIPE_DISPLAYS.register("brewing", () -> BrewingRecipeDisplay.SERIALIZER);
    }
}

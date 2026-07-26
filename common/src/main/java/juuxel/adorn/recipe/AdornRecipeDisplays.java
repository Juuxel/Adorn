package juuxel.adorn.recipe;

import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.core.registries.Registries;

public final class AdornRecipeDisplays {
    public static final Registrar<RecipeDisplay.Type<?>> RECIPE_DISPLAYS = RegistrarFactory.get().create(Registries.RECIPE_DISPLAY);

    public static void init() {
        RECIPE_DISPLAYS.register("brewing", () -> BrewingRecipeDisplay.SERIALIZER);
    }
}

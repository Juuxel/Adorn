package juuxel.adorn.recipe;

import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;

public final class AdornRecipeSerializers {
    public static final Registrar<RecipeSerializer<?>> RECIPE_SERIALIZERS = RegistrarFactory.get().create(Registries.RECIPE_SERIALIZER);

    public static final Registered<RecipeSerializer<ItemBrewingRecipe>> BREWING =
        RECIPE_SERIALIZERS.register("brewing", () -> ItemBrewingRecipe.SERIALIZER);
    public static final Registered<RecipeSerializer<FluidBrewingRecipe>> BREWING_FROM_FLUID =
        RECIPE_SERIALIZERS.register("brewing_from_fluid", () -> FluidBrewingRecipe.SERIALIZER);
    public static final Registered<RecipeSerializer<FertilizerRefillingRecipe>> FERTILIZER_REFILLING =
        RECIPE_SERIALIZERS.register("fertilizer_refilling", () -> FertilizerRefillingRecipe.SERIALIZER);

    public static void init() {
    }
}

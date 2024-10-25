package juuxel.adorn.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import juuxel.adorn.fluid.FluidIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

import static juuxel.adorn.block.entity.BrewerBlockEntity.LEFT_INGREDIENT_SLOT;
import static juuxel.adorn.block.entity.BrewerBlockEntity.RIGHT_INGREDIENT_SLOT;

public record FluidBrewingRecipe(Ingredient firstIngredient, Ingredient secondIngredient, FluidIngredient fluid, ItemStack result) implements BrewingRecipe {
    public static final MapCodec<FluidBrewingRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
        Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("first_ingredient").forGetter(FluidBrewingRecipe::firstIngredient),
        Ingredient.ALLOW_EMPTY_CODEC.optionalFieldOf("second_ingredient", Ingredient.empty()).forGetter(FluidBrewingRecipe::secondIngredient),
        FluidIngredient.CODEC.fieldOf("fluid").forGetter(FluidBrewingRecipe::fluid),
        ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(FluidBrewingRecipe::result)
    ).apply(builder, FluidBrewingRecipe::new));

    @Override
    public boolean matches(BrewerInput input, World world) {
        var itemsMatch = (matches(input, LEFT_INGREDIENT_SLOT, firstIngredient) && matches(input, RIGHT_INGREDIENT_SLOT, secondIngredient)) ||
            (matches(input, RIGHT_INGREDIENT_SLOT, firstIngredient) && matches(input, LEFT_INGREDIENT_SLOT, secondIngredient));
        return itemsMatch && input.getFluidReference().matches(fluid);
    }

    private static boolean matches(BrewerInput input, int index, Ingredient ingredient) {
        return ingredient.test(input.getStackInSlot(index));
    }

    @Override
    public ItemStack craft(BrewerInput input, RegistryWrapper.WrapperLookup registries) {
        return result.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registries) {
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AdornRecipeSerializers.BREWING_FROM_FLUID.get();
    }

    public static final class Serializer implements RecipeSerializer<FluidBrewingRecipe> {
        private static final PacketCodec<RegistryByteBuf, FluidBrewingRecipe> PACKET_CODEC = PacketCodec.tuple(
            Ingredient.PACKET_CODEC,
            FluidBrewingRecipe::firstIngredient,
            Ingredient.PACKET_CODEC,
            FluidBrewingRecipe::secondIngredient,
            FluidIngredient.PACKET_CODEC,
            FluidBrewingRecipe::fluid,
            ItemStack.PACKET_CODEC,
            FluidBrewingRecipe::result,
            FluidBrewingRecipe::new
        );

        @Override
        public MapCodec<FluidBrewingRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, FluidBrewingRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }
}

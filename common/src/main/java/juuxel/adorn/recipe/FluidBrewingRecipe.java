package juuxel.adorn.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.fluid.FluidIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

import static juuxel.adorn.block.entity.BrewerBlockEntity.LEFT_INGREDIENT_SLOT;
import static juuxel.adorn.block.entity.BrewerBlockEntity.RIGHT_INGREDIENT_SLOT;

public record FluidBrewingRecipe(Ingredient firstIngredient, Optional<Ingredient> secondIngredient, FluidIngredient fluid, ItemStack result) implements BrewingRecipe {
    public static final MapCodec<FluidBrewingRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
        Ingredient.CODEC.fieldOf("first_ingredient").forGetter(FluidBrewingRecipe::firstIngredient),
        Ingredient.CODEC.optionalFieldOf("second_ingredient").forGetter(FluidBrewingRecipe::secondIngredient),
        FluidIngredient.CODEC.fieldOf("fluid").forGetter(FluidBrewingRecipe::fluid),
        ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(FluidBrewingRecipe::result)
    ).apply(builder, FluidBrewingRecipe::new));

    @Override
    public boolean matches(BrewerInput input, World world) {
        var itemsMatch = (input.matches(LEFT_INGREDIENT_SLOT, firstIngredient) && input.matches(RIGHT_INGREDIENT_SLOT, secondIngredient)) ||
            (input.matches(RIGHT_INGREDIENT_SLOT, firstIngredient) && input.matches(LEFT_INGREDIENT_SLOT, secondIngredient));
        return itemsMatch && input.getFluidReference().matches(fluid);
    }

    @Override
    public ItemStack craft(BrewerInput input, RegistryWrapper.WrapperLookup registries) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<FluidBrewingRecipe> getSerializer() {
        return AdornRecipeSerializers.BREWING_FROM_FLUID.get();
    }

    @Override
    public List<RecipeDisplay> getDisplays() {
        return List.of(
            new BrewingRecipeDisplay(
                firstIngredient.toDisplay(),
                secondIngredient.map(Ingredient::toDisplay).orElse(SlotDisplay.EmptySlotDisplay.INSTANCE),
                new FluidIngredientSlotDisplay(fluid),
                new SlotDisplay.StackSlotDisplay(result),
                new SlotDisplay.ItemSlotDisplay(AdornBlocks.BREWER.get().asItem())
            )
        );
    }

    public static final class Serializer implements RecipeSerializer<FluidBrewingRecipe> {
        private static final PacketCodec<RegistryByteBuf, FluidBrewingRecipe> PACKET_CODEC = PacketCodec.tuple(
            Ingredient.PACKET_CODEC,
            FluidBrewingRecipe::firstIngredient,
            Ingredient.OPTIONAL_PACKET_CODEC,
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

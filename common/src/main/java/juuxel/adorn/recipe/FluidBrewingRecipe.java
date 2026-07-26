package juuxel.adorn.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.fluid.FluidIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

import static juuxel.adorn.block.entity.BrewerBlockEntity.INPUT_SLOT;
import static juuxel.adorn.block.entity.BrewerBlockEntity.LEFT_INGREDIENT_SLOT;
import static juuxel.adorn.block.entity.BrewerBlockEntity.RIGHT_INGREDIENT_SLOT;

public record FluidBrewingRecipe(
    Ingredient input, Ingredient firstIngredient, Optional<Ingredient> secondIngredient, FluidIngredient fluid, ItemStack result) implements BrewingRecipe {
    public static final MapCodec<FluidBrewingRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
        Ingredient.CODEC.fieldOf("input").forGetter(FluidBrewingRecipe::input),
        Ingredient.CODEC.fieldOf("first_ingredient").forGetter(FluidBrewingRecipe::firstIngredient),
        Ingredient.CODEC.optionalFieldOf("second_ingredient").forGetter(FluidBrewingRecipe::secondIngredient),
        FluidIngredient.CODEC.fieldOf("fluid").forGetter(FluidBrewingRecipe::fluid),
        ItemStack.STRICT_CODEC.fieldOf("result").forGetter(FluidBrewingRecipe::result)
    ).apply(builder, FluidBrewingRecipe::new));

    @Override
    public boolean matches(BrewerInput input, Level world) {
        var ingredientsMatch = (input.matches(LEFT_INGREDIENT_SLOT, firstIngredient) && input.matches(RIGHT_INGREDIENT_SLOT, secondIngredient)) ||
            (input.matches(RIGHT_INGREDIENT_SLOT, firstIngredient) && input.matches(LEFT_INGREDIENT_SLOT, secondIngredient));
        return ingredientsMatch && input.matches(INPUT_SLOT, this.input) && input.getFluidReference().matches(fluid);
    }

    @Override
    public ItemStack assemble(BrewerInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<FluidBrewingRecipe> getSerializer() {
        return AdornRecipeSerializers.BREWING_FROM_FLUID.get();
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(
            new BrewingRecipeDisplay(
                input.display(),
                firstIngredient.display(),
                secondIngredient.map(Ingredient::display).orElse(SlotDisplay.Empty.INSTANCE),
                new FluidIngredientSlotDisplay(fluid),
                new SlotDisplay.ItemStackSlotDisplay(result),
                new SlotDisplay.ItemSlotDisplay(AdornBlocks.BREWER.get().asItem())
            )
        );
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    public static final class Serializer implements RecipeSerializer<FluidBrewingRecipe> {
        private static final StreamCodec<RegistryFriendlyByteBuf, FluidBrewingRecipe> PACKET_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            FluidBrewingRecipe::input,
            Ingredient.CONTENTS_STREAM_CODEC,
            FluidBrewingRecipe::firstIngredient,
            Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC,
            FluidBrewingRecipe::secondIngredient,
            FluidIngredient.PACKET_CODEC,
            FluidBrewingRecipe::fluid,
            ItemStack.STREAM_CODEC,
            FluidBrewingRecipe::result,
            FluidBrewingRecipe::new
        );

        @Override
        public MapCodec<FluidBrewingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FluidBrewingRecipe> streamCodec() {
            return PACKET_CODEC;
        }
    }
}

package juuxel.adorn.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import juuxel.adorn.block.AdornBlocks;
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

public record ItemBrewingRecipe(Ingredient firstIngredient, Optional<Ingredient> secondIngredient, ItemStack result) implements BrewingRecipe {
    public static final MapCodec<ItemBrewingRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
        Ingredient.CODEC.fieldOf("first_ingredient").forGetter(ItemBrewingRecipe::firstIngredient),
        Ingredient.CODEC.optionalFieldOf("second_ingredient").forGetter(ItemBrewingRecipe::secondIngredient),
        ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(ItemBrewingRecipe::result)
    ).apply(builder, ItemBrewingRecipe::new));

    @Override
    public boolean matches(BrewerInput input, World world) {
        return (input.matches(LEFT_INGREDIENT_SLOT, firstIngredient) && input.matches(RIGHT_INGREDIENT_SLOT, secondIngredient)) ||
            (input.matches(RIGHT_INGREDIENT_SLOT, firstIngredient) && input.matches(LEFT_INGREDIENT_SLOT, secondIngredient));
    }

    @Override
    public ItemStack craft(BrewerInput input, RegistryWrapper.WrapperLookup registries) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<ItemBrewingRecipe> getSerializer() {
        return AdornRecipeSerializers.BREWING.get();
    }

    @Override
    public List<RecipeDisplay> getDisplays() {
        return List.of(
            new BrewingRecipeDisplay(
                firstIngredient.toDisplay(),
                secondIngredient.map(Ingredient::toDisplay).orElse(SlotDisplay.EmptySlotDisplay.INSTANCE),
                SlotDisplay.EmptySlotDisplay.INSTANCE,
                new SlotDisplay.StackSlotDisplay(result),
                new SlotDisplay.ItemSlotDisplay(AdornBlocks.BREWER.get().asItem())
            )
        );
    }

    public static final class Serializer implements RecipeSerializer<ItemBrewingRecipe> {
        private static final PacketCodec<RegistryByteBuf, ItemBrewingRecipe> PACKET_CODEC = PacketCodec.tuple(
            Ingredient.PACKET_CODEC,
            ItemBrewingRecipe::firstIngredient,
            Ingredient.OPTIONAL_PACKET_CODEC,
            ItemBrewingRecipe::secondIngredient,
            ItemStack.PACKET_CODEC,
            ItemBrewingRecipe::result,
            ItemBrewingRecipe::new
        );

        @Override
        public MapCodec<ItemBrewingRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ItemBrewingRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }
}

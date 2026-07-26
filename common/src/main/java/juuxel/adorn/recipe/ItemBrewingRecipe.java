package juuxel.adorn.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import juuxel.adorn.block.AdornBlocks;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

import static juuxel.adorn.block.entity.BrewerBlockEntity.*;

public record ItemBrewingRecipe(
    CommonInfo commonInfo,
    Ingredient input,
    Ingredient firstIngredient,
    Optional<Ingredient> secondIngredient,
    ItemStackTemplate result
) implements BrewingRecipe {
    public static final MapCodec<ItemBrewingRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
        CommonInfo.MAP_CODEC.forGetter(ItemBrewingRecipe::commonInfo),
        Ingredient.CODEC.fieldOf("input").forGetter(ItemBrewingRecipe::input),
        Ingredient.CODEC.fieldOf("first_ingredient").forGetter(ItemBrewingRecipe::firstIngredient),
        Ingredient.CODEC.optionalFieldOf("second_ingredient").forGetter(ItemBrewingRecipe::secondIngredient),
        ItemStackTemplate.CODEC.fieldOf("result").forGetter(ItemBrewingRecipe::result)
    ).apply(builder, ItemBrewingRecipe::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, ItemBrewingRecipe> STREAM_CODEC = StreamCodec.composite(
        CommonInfo.STREAM_CODEC,
        ItemBrewingRecipe::commonInfo,
        Ingredient.CONTENTS_STREAM_CODEC,
        ItemBrewingRecipe::input,
        Ingredient.CONTENTS_STREAM_CODEC,
        ItemBrewingRecipe::firstIngredient,
        Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC,
        ItemBrewingRecipe::secondIngredient,
        ItemStackTemplate.STREAM_CODEC,
        ItemBrewingRecipe::result,
        ItemBrewingRecipe::new
    );

    public static final RecipeSerializer<ItemBrewingRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    @Override
    public boolean matches(BrewerInput input, Level world) {
        var ingredientsMatch = (input.matches(LEFT_INGREDIENT_SLOT, firstIngredient) && input.matches(RIGHT_INGREDIENT_SLOT, secondIngredient)) ||
            (input.matches(RIGHT_INGREDIENT_SLOT, firstIngredient) && input.matches(LEFT_INGREDIENT_SLOT, secondIngredient));
        return ingredientsMatch && input.matches(INPUT_SLOT, this.input);
    }

    @Override
    public ItemStack assemble(BrewerInput input) {
        return result.create();
    }

    @Override
    public boolean showNotification() {
        return commonInfo.showNotification();
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeSerializer<ItemBrewingRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(
            new BrewingRecipeDisplay(
                input.display(),
                firstIngredient.display(),
                secondIngredient.map(Ingredient::display).orElse(SlotDisplay.Empty.INSTANCE),
                SlotDisplay.Empty.INSTANCE,
                new SlotDisplay.ItemStackSlotDisplay(result),
                new SlotDisplay.ItemSlotDisplay(AdornBlocks.BREWER.get().asItem())
            )
        );
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.createFromOptionals(List.of(Optional.of(input), Optional.of(firstIngredient), secondIngredient));
    }
}

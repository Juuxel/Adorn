package juuxel.adorn.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.fluid.FluidIngredient;
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

public record FluidBrewingRecipe(
    CommonInfo commonInfo,
    Ingredient input,
    Ingredient firstIngredient,
    Optional<Ingredient> secondIngredient,
    FluidIngredient fluid,
    ItemStackTemplate result
) implements BrewingRecipe {
    public static final MapCodec<FluidBrewingRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
        CommonInfo.MAP_CODEC.forGetter(FluidBrewingRecipe::commonInfo),
        Ingredient.CODEC.fieldOf("input").forGetter(FluidBrewingRecipe::input),
        Ingredient.CODEC.fieldOf("first_ingredient").forGetter(FluidBrewingRecipe::firstIngredient),
        Ingredient.CODEC.optionalFieldOf("second_ingredient").forGetter(FluidBrewingRecipe::secondIngredient),
        FluidIngredient.CODEC.fieldOf("fluid").forGetter(FluidBrewingRecipe::fluid),
        ItemStackTemplate.CODEC.fieldOf("result").forGetter(FluidBrewingRecipe::result)
    ).apply(builder, FluidBrewingRecipe::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, FluidBrewingRecipe> STREAM_CODEC = StreamCodec.composite(
        CommonInfo.STREAM_CODEC,
        FluidBrewingRecipe::commonInfo,
        Ingredient.CONTENTS_STREAM_CODEC,
        FluidBrewingRecipe::input,
        Ingredient.CONTENTS_STREAM_CODEC,
        FluidBrewingRecipe::firstIngredient,
        Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC,
        FluidBrewingRecipe::secondIngredient,
        FluidIngredient.PACKET_CODEC,
        FluidBrewingRecipe::fluid,
        ItemStackTemplate.STREAM_CODEC,
        FluidBrewingRecipe::result,
        FluidBrewingRecipe::new
    );

    public static final RecipeSerializer<FluidBrewingRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    @Override
    public boolean matches(BrewerInput input, Level world) {
        var ingredientsMatch = (input.matches(LEFT_INGREDIENT_SLOT, firstIngredient) && input.matches(RIGHT_INGREDIENT_SLOT, secondIngredient)) ||
            (input.matches(RIGHT_INGREDIENT_SLOT, firstIngredient) && input.matches(LEFT_INGREDIENT_SLOT, secondIngredient));
        return ingredientsMatch && input.matches(INPUT_SLOT, this.input) && input.getFluidReference().matches(fluid);
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
    public RecipeSerializer<FluidBrewingRecipe> getSerializer() {
        return SERIALIZER;
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
}

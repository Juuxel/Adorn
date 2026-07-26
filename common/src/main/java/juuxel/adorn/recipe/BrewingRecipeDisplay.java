package juuxel.adorn.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

public record BrewingRecipeDisplay(SlotDisplay input, SlotDisplay firstIngredient, SlotDisplay secondIngredient, SlotDisplay fluid, SlotDisplay result, SlotDisplay craftingStation) implements RecipeDisplay {
    public static final MapCodec<BrewingRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        SlotDisplay.CODEC.fieldOf("input").forGetter(BrewingRecipeDisplay::input),
        SlotDisplay.CODEC.fieldOf("first_ingredient").forGetter(BrewingRecipeDisplay::firstIngredient),
        SlotDisplay.CODEC.fieldOf("second_ingredient").forGetter(BrewingRecipeDisplay::secondIngredient),
        SlotDisplay.CODEC.fieldOf("fluid").forGetter(BrewingRecipeDisplay::fluid),
        SlotDisplay.CODEC.fieldOf("result").forGetter(BrewingRecipeDisplay::result),
        SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(BrewingRecipeDisplay::craftingStation)
    ).apply(instance, BrewingRecipeDisplay::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, BrewingRecipeDisplay> PACKET_CODEC = StreamCodec.composite(
        SlotDisplay.STREAM_CODEC, BrewingRecipeDisplay::input,
        SlotDisplay.STREAM_CODEC, BrewingRecipeDisplay::firstIngredient,
        SlotDisplay.STREAM_CODEC, BrewingRecipeDisplay::secondIngredient,
        SlotDisplay.STREAM_CODEC, BrewingRecipeDisplay::fluid,
        SlotDisplay.STREAM_CODEC, BrewingRecipeDisplay::result,
        SlotDisplay.STREAM_CODEC, BrewingRecipeDisplay::craftingStation,
        BrewingRecipeDisplay::new
    );

    public static final Type<BrewingRecipeDisplay> SERIALIZER = new Type<>(MAP_CODEC, PACKET_CODEC);

    @Override
    public boolean isEnabled(FeatureFlagSet features) {
        return input.isEnabled(features) && firstIngredient.isEnabled(features) && secondIngredient.isEnabled(features) && RecipeDisplay.super.isEnabled(features);
    }

    @Override
    public Type<? extends RecipeDisplay> type() {
        return SERIALIZER;
    }
}

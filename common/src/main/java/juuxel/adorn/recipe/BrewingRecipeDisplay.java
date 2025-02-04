package juuxel.adorn.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.resource.featuretoggle.FeatureSet;

public record BrewingRecipeDisplay(SlotDisplay firstIngredient, SlotDisplay secondIngredient, SlotDisplay fluid, SlotDisplay result, SlotDisplay craftingStation) implements RecipeDisplay {
    public static final MapCodec<BrewingRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        SlotDisplay.CODEC.fieldOf("first_ingredient").forGetter(BrewingRecipeDisplay::firstIngredient),
        SlotDisplay.CODEC.fieldOf("second_ingredient").forGetter(BrewingRecipeDisplay::secondIngredient),
        SlotDisplay.CODEC.fieldOf("fluid").forGetter(BrewingRecipeDisplay::fluid),
        SlotDisplay.CODEC.fieldOf("result").forGetter(BrewingRecipeDisplay::result),
        SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(BrewingRecipeDisplay::craftingStation)
    ).apply(instance, BrewingRecipeDisplay::new));

    public static final PacketCodec<RegistryByteBuf, BrewingRecipeDisplay> PACKET_CODEC = PacketCodec.tuple(
        SlotDisplay.PACKET_CODEC, BrewingRecipeDisplay::firstIngredient,
        SlotDisplay.PACKET_CODEC, BrewingRecipeDisplay::secondIngredient,
        SlotDisplay.PACKET_CODEC, BrewingRecipeDisplay::fluid,
        SlotDisplay.PACKET_CODEC, BrewingRecipeDisplay::result,
        SlotDisplay.PACKET_CODEC, BrewingRecipeDisplay::craftingStation,
        BrewingRecipeDisplay::new
    );

    public static final Serializer<BrewingRecipeDisplay> SERIALIZER = new Serializer<>(MAP_CODEC, PACKET_CODEC);

    @Override
    public boolean isEnabled(FeatureSet features) {
        return firstIngredient.isEnabled(features) && secondIngredient.isEnabled(features) && RecipeDisplay.super.isEnabled(features);
    }

    @Override
    public Serializer<? extends RecipeDisplay> serializer() {
        return SERIALIZER;
    }
}

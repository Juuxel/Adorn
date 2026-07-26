package juuxel.adorn.recipe;

import com.mojang.serialization.MapCodec;
import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.item.AdornItems;
import juuxel.adorn.item.WateringCanItem;
import juuxel.adorn.lib.AdornTags;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public final class FertilizerRefillingRecipe extends CustomRecipe {
    public static final FertilizerRefillingRecipe INSTANCE = new FertilizerRefillingRecipe();
    public static final MapCodec<FertilizerRefillingRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, FertilizerRefillingRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final RecipeSerializer<FertilizerRefillingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    private FertilizerRefillingRecipe() {
    }

    @Override
    public boolean matches(CraftingInput input, Level world) {
        return match(input) != null;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        var match = match(input);
        if (match == null) return ItemStack.EMPTY;

        var result = match.wateringCan().copy();
        int fertilizerLevel = WateringCanItem.FertilizerLevel.get(result);
        int newFertilizerLevel = Math.min(fertilizerLevel + match.fertilizers(), WateringCanItem.MAX_FERTILIZER_LEVEL);
        result.set(AdornComponentTypes.FERTILIZER_LEVEL.get(), WateringCanItem.FertilizerLevel.of(newFertilizerLevel));
        return result;
    }

    private @Nullable MatchResult match(CraftingInput inventory) {
        var wateringCan = ItemStack.EMPTY;
        var fertilizers = 0;

        for (int slot = 0; slot < inventory.size(); slot++) {
            var stack = inventory.getItem(slot);

            if (stack.is(AdornItems.WATERING_CAN.get())) {
                if (wateringCan.isEmpty()) {
                    wateringCan = stack;
                } else {
                    // We don't want double watering cans
                    return null;
                }
            } else if (!stack.isEmpty()) {
                if (stack.is(AdornTags.WATERING_CAN_FERTILIZERS)) {
                    fertilizers++;
                } else {
                    // Unwanted item
                    return null;
                }
            }
        }

        // Not a successful match if we don't have a watering can at all or no fertilizer to fill it with
        if (wateringCan.isEmpty() || fertilizers == 0) return null;

        return new MatchResult(wateringCan, fertilizers);
    }

    @Override
    public RecipeSerializer<FertilizerRefillingRecipe> getSerializer() {
        return SERIALIZER;
    }

    private record MatchResult(ItemStack wateringCan, int fertilizers) {
    }
}

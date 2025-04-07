package juuxel.adorn.recipe;

import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.item.AdornItems;
import juuxel.adorn.item.WateringCanItem;
import juuxel.adorn.lib.AdornTags;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public final class FertilizerRefillingRecipe extends SpecialCraftingRecipe {
    public FertilizerRefillingRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        return match(input) != null;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        var match = match(input);
        if (match == null) return ItemStack.EMPTY;

        var result = match.wateringCan().copy();
        int fertilizerLevel = WateringCanItem.FertilizerLevel.get(result);
        int newFertilizerLevel = Math.min(fertilizerLevel + match.fertilizers(), WateringCanItem.MAX_FERTILIZER_LEVEL);
        result.set(AdornComponentTypes.FERTILIZER_LEVEL.get(), WateringCanItem.FertilizerLevel.of(newFertilizerLevel));
        return result;
    }

    private @Nullable MatchResult match(CraftingRecipeInput inventory) {
        var wateringCan = ItemStack.EMPTY;
        var fertilizers = 0;

        for (int slot = 0; slot < inventory.size(); slot++) {
            var stack = inventory.getStackInSlot(slot);

            if (stack.isOf(AdornItems.WATERING_CAN.get())) {
                if (wateringCan.isEmpty()) {
                    wateringCan = stack;
                } else {
                    // We don't want double watering cans
                    return null;
                }
            } else if (!stack.isEmpty()) {
                if (stack.isIn(AdornTags.WATERING_CAN_FERTILIZERS)) {
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
        return AdornRecipeSerializers.FERTILIZER_REFILLING.get();
    }

    private record MatchResult(ItemStack wateringCan, int fertilizers) {
    }
}

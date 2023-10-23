package juuxel.adorn.recipe

import juuxel.adorn.item.AdornItems
import juuxel.adorn.item.WateringCanItem
import juuxel.adorn.lib.AdornTags
import net.minecraft.inventory.RecipeInputInventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.SpecialCraftingRecipe
import net.minecraft.recipe.book.CraftingRecipeCategory
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.world.World
import kotlin.math.min

class FertilizerRefillingRecipe(category: CraftingRecipeCategory) : SpecialCraftingRecipe(category) {
    override fun matches(inventory: RecipeInputInventory, world: World): Boolean =
        match(inventory) != null

    override fun craft(inventory: RecipeInputInventory, registryManager: DynamicRegistryManager): ItemStack {
        val (wateringCan, fertilizers) = match(inventory) ?: return ItemStack.EMPTY

        val result = wateringCan.copy()
        val nbt = result.getOrCreateNbt()
        val fertilizerLevel = nbt.getInt(WateringCanItem.NBT_FERTILIZER_LEVEL)
        val newFertilizerLevel = min(fertilizerLevel + fertilizers, WateringCanItem.MAX_FERTILIZER_LEVEL)
        nbt.putInt(WateringCanItem.NBT_FERTILIZER_LEVEL, newFertilizerLevel)
        return result
    }

    private fun match(inventory: RecipeInputInventory): MatchResult? {
        var wateringCan: ItemStack = ItemStack.EMPTY
        var fertilizers = 0

        for (slot in 0 until inventory.size()) {
            val stack = inventory.getStack(slot)

            if (stack.isOf(AdornItems.WATERING_CAN)) {
                if (wateringCan.isEmpty) {
                    wateringCan = stack
                } else {
                    // We don't want double watering cans
                    return null
                }
            } else if (!stack.isEmpty) {
                if (stack.isIn(AdornTags.WATERING_CAN_FERTILIZERS)) {
                    fertilizers++
                } else {
                    // Unwanted item
                    return null
                }
            }
        }

        // Not a successful match if we don't have a watering can at all or no fertilizer to fill it with
        if (wateringCan.isEmpty || fertilizers == 0) return null

        return MatchResult(wateringCan, fertilizers)
    }

    override fun fits(width: Int, height: Int): Boolean =
        width * height >= 2

    override fun getSerializer(): RecipeSerializer<*> =
        AdornRecipes.FERTILIZER_REFILLING_SERIALIZER

    private data class MatchResult(val wateringCan: ItemStack, val fertilizers: Int)
}

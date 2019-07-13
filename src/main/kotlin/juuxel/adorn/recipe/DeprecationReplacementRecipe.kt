package juuxel.adorn.recipe

import com.google.gson.JsonObject
import juuxel.adorn.item.DeprecatedItem
import juuxel.adorn.lib.ModRecipes
import net.minecraft.inventory.CraftingInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.*
import net.minecraft.util.DefaultedList
import net.minecraft.util.Identifier
import net.minecraft.util.PacketByteBuf
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

/**
 * A recipe type for converting deprecated items to their newer versions.
 * Example: `c:stone_rod` -> `adorn:stone_rod`
 */
class DeprecationReplacementRecipe(id: Identifier, private val from: Item, private val to: Item) :
    ShapelessRecipe(id, "", ItemStack(to), DefaultedList.create(1, Ingredient.ofItems(from))) {
    // hmm
    override fun matches(p0: CraftingInventory?, p1: World?) = super.method_17730(p0, p1)

    override fun craft(p0: CraftingInventory?) = super.method_17729(p0)

    override fun isIgnoredInRecipeBook() = true

    override fun getSerializer() = ModRecipes.DEPRECATION_REPLACEMENT_SERIALIZER

    class Serializer : RecipeSerializer<DeprecationReplacementRecipe> {
        override fun write(buf: PacketByteBuf, recipe: DeprecationReplacementRecipe) {
            buf.writeIdentifier(Registry.ITEM.getId(recipe.from))
            buf.writeIdentifier(Registry.ITEM.getId(recipe.to))
        }

        override fun read(id: Identifier, obj: JsonObject) =
            DeprecationReplacementRecipe(
                id,
                Identifier(obj["from"].asString).let { itemId ->
                    Registry.ITEM.getOrEmpty(itemId)
                        .filter { it is DeprecatedItem }
                        .orElse(Items.AIR)
                },
                Registry.ITEM[Identifier(obj["to"].asString)]
            )

        override fun read(id: Identifier, buf: PacketByteBuf) =
            DeprecationReplacementRecipe(
                id,
                Registry.ITEM[buf.readIdentifier()],
                Registry.ITEM[buf.readIdentifier()]
            )
    }
}

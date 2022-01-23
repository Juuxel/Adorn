package juuxel.adorn.recipe

import com.google.gson.JsonObject
import juuxel.adorn.block.entity.BrewerBlockEntity.Companion.LEFT_INGREDIENT_SLOT
import juuxel.adorn.block.entity.BrewerBlockEntity.Companion.RIGHT_INGREDIENT_SLOT
import juuxel.adorn.util.ForgeRegistryEntryImpl
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.ShapedRecipe
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper
import net.minecraft.world.World

class BrewingRecipe(
    private val id: Identifier,
    val firstIngredient: Ingredient,
    val secondIngredient: Ingredient,
    val result: ItemStack
) : Recipe<Inventory> {
    override fun matches(inventory: Inventory, world: World): Boolean {
        fun matches(index: Int, ingredient: Ingredient) =
            ingredient.test(inventory.getStack(index))

        return (matches(LEFT_INGREDIENT_SLOT, firstIngredient) && matches(RIGHT_INGREDIENT_SLOT, secondIngredient)) ||
            (matches(RIGHT_INGREDIENT_SLOT, firstIngredient) && matches(LEFT_INGREDIENT_SLOT, secondIngredient))
    }

    override fun craft(inventory: Inventory): ItemStack = result.copy()
    override fun fits(width: Int, height: Int): Boolean = true
    override fun getOutput(): ItemStack = result
    override fun getId(): Identifier = id
    override fun getSerializer(): RecipeSerializer<*> = AdornRecipes.BREWING_SERIALIZER
    override fun getType(): RecipeType<*> = AdornRecipes.BREWING_TYPE

    class Serializer : ForgeRegistryEntryImpl(RecipeSerializer::class.java), RecipeSerializer<BrewingRecipe> {
        override fun read(id: Identifier, json: JsonObject): BrewingRecipe {
            val first = Ingredient.fromJson(json["first_ingredient"])
            val secondJson = json["second_ingredient"]
            val second = if (secondJson != null) Ingredient.fromJson(secondJson) else Ingredient.empty()
            val result = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"))
            return BrewingRecipe(id, first, second, result)
        }

        override fun read(id: Identifier, buf: PacketByteBuf): BrewingRecipe {
            val first = Ingredient.fromPacket(buf)
            val second = Ingredient.fromPacket(buf)
            val output = buf.readItemStack()
            return BrewingRecipe(id, first, second, output)
        }

        override fun write(buf: PacketByteBuf, recipe: BrewingRecipe) {
            recipe.firstIngredient.write(buf)
            recipe.secondIngredient.write(buf)
            buf.writeItemStack(recipe.result)
        }
    }
}

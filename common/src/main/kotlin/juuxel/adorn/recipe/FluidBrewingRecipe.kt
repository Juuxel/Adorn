package juuxel.adorn.recipe

import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import juuxel.adorn.block.entity.BrewerBlockEntity.Companion.LEFT_INGREDIENT_SLOT
import juuxel.adorn.block.entity.BrewerBlockEntity.Companion.RIGHT_INGREDIENT_SLOT
import juuxel.adorn.fluid.FluidIngredient
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.ShapedRecipe
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper
import net.minecraft.world.World

class FluidBrewingRecipe(
    private val id: Identifier,
    val firstIngredient: Ingredient,
    val secondIngredient: Ingredient,
    val fluid: FluidIngredient,
    val result: ItemStack
) : BrewingRecipe {
    override fun matches(inventory: BrewerInventory, world: World): Boolean {
        fun matches(index: Int, ingredient: Ingredient) =
            ingredient.test(inventory.getStack(index))

        val itemsMatch = (matches(LEFT_INGREDIENT_SLOT, firstIngredient) && matches(RIGHT_INGREDIENT_SLOT, secondIngredient)) ||
            (matches(RIGHT_INGREDIENT_SLOT, firstIngredient) && matches(LEFT_INGREDIENT_SLOT, firstIngredient))
        return itemsMatch && inventory.fluidReference.matches(fluid)
    }

    override fun craft(inventory: BrewerInventory, registryManager: DynamicRegistryManager): ItemStack = result.copy()
    override fun fits(width: Int, height: Int): Boolean = true
    override fun getOutput(registryManager: DynamicRegistryManager): ItemStack = result
    override fun getId(): Identifier = id
    override fun getSerializer(): RecipeSerializer<*> = AdornRecipes.BREWING_FROM_FLUID_SERIALIZER

    class Serializer : RecipeSerializer<FluidBrewingRecipe> {
        override fun read(id: Identifier, json: JsonObject): FluidBrewingRecipe {
            val first = Ingredient.fromJson(json["first_ingredient"])
            val secondJson = json["second_ingredient"]
            val second = if (secondJson != null) Ingredient.fromJson(secondJson) else Ingredient.empty()
            val fluidJson = json["fluid"]
            val fluid = FluidIngredient.CODEC.decode(JsonOps.INSTANCE, fluidJson)
                .getOrThrow(false) { throw RuntimeException("Could not read fluid brewing recipe $id: $it") }
                .first
            val result = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"))
            return FluidBrewingRecipe(id, first, second, fluid, result)
        }

        override fun read(id: Identifier, buf: PacketByteBuf): FluidBrewingRecipe {
            val first = Ingredient.fromPacket(buf)
            val second = Ingredient.fromPacket(buf)
            val fluid = FluidIngredient.load(buf)
            val output = buf.readItemStack()
            return FluidBrewingRecipe(id, first, second, fluid, output)
        }

        override fun write(buf: PacketByteBuf, recipe: FluidBrewingRecipe) {
            recipe.firstIngredient.write(buf)
            recipe.secondIngredient.write(buf)
            recipe.fluid.write(buf)
            buf.writeItemStack(recipe.result)
        }
    }
}

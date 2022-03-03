package juuxel.adorn.recipe

import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import juuxel.adorn.block.entity.BrewerBlockEntity.Companion.LEFT_INGREDIENT_SLOT
import juuxel.adorn.block.entity.BrewerBlockEntity.Companion.RIGHT_INGREDIENT_SLOT
import juuxel.adorn.fluid.FluidReference
import juuxel.adorn.fluid.FluidUnit
import juuxel.adorn.fluid.FluidVolume
import juuxel.adorn.util.ForgeRegistryEntryImpl
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.ShapedRecipe
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper
import net.minecraft.world.World

class FluidBrewingRecipe(
    private val id: Identifier,
    val firstIngredient: Ingredient,
    val fluid: FluidVolume,
    val result: ItemStack
) : IBrewingRecipe {
    override fun matches(inventory: BrewerInventory, world: World): Boolean {
        fun matches(index: Int, ingredient: Ingredient) =
            ingredient.test(inventory.getStack(index))

        return (matches(LEFT_INGREDIENT_SLOT, firstIngredient) || matches(RIGHT_INGREDIENT_SLOT, firstIngredient)) &&
            FluidReference.areFluidsEqual(inventory.fluidReference, fluid) &&
            FluidUnit.compareVolumes(inventory.fluidReference, fluid) >= 0
    }

    override fun craft(inventory: BrewerInventory): ItemStack = result.copy()
    override fun fits(width: Int, height: Int): Boolean = true
    override fun getOutput(): ItemStack = result
    override fun getId(): Identifier = id
    override fun getSerializer(): RecipeSerializer<*> = AdornRecipes.BREWING_SERIALIZER

    class Serializer : ForgeRegistryEntryImpl<RecipeSerializer<*>>(RecipeSerializer::class.java), RecipeSerializer<FluidBrewingRecipe> {
        override fun read(id: Identifier, json: JsonObject): FluidBrewingRecipe {
            val first = Ingredient.fromJson(json["first_ingredient"])
            val fluidJson = json["fluid"]
            val fluid = FluidVolume.CODEC.decode(JsonOps.INSTANCE, fluidJson)
                .getOrThrow(false) { throw RuntimeException("Could not read fluid brewing recipe $id: $it") }
                .first
            val result = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"))
            return FluidBrewingRecipe(id, first, fluid, result)
        }

        override fun read(id: Identifier, buf: PacketByteBuf): FluidBrewingRecipe {
            val first = Ingredient.fromPacket(buf)
            val fluid = FluidVolume.load(buf)
            val output = buf.readItemStack()
            return FluidBrewingRecipe(id, first, fluid, output)
        }

        override fun write(buf: PacketByteBuf, recipe: FluidBrewingRecipe) {
            recipe.firstIngredient.write(buf)
            recipe.fluid.write(buf)
            buf.writeItemStack(recipe.result)
        }
    }
}

package juuxel.adorn.recipe

import com.google.gson.JsonObject
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import com.mojang.serialization.codecs.RecordCodecBuilder
import juuxel.adorn.block.entity.BrewerBlockEntity.Companion.LEFT_INGREDIENT_SLOT
import juuxel.adorn.block.entity.BrewerBlockEntity.Companion.RIGHT_INGREDIENT_SLOT
import juuxel.adorn.fluid.FluidIngredient
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeCodecs
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.ShapedRecipe
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper
import net.minecraft.world.World

class FluidBrewingRecipe(
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
    override fun getResult(registryManager: DynamicRegistryManager): ItemStack = result
    override fun getSerializer(): RecipeSerializer<*> = AdornRecipes.BREWING_FROM_FLUID_SERIALIZER

    companion object {
        val CODEC: Codec<FluidBrewingRecipe> = RecordCodecBuilder.create { builder ->
            builder.group(
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("first_ingredient").forGetter { it.firstIngredient },
                Ingredient.ALLOW_EMPTY_CODEC.optionalFieldOf("second_ingredient", Ingredient.empty()).forGetter { it.secondIngredient },
                FluidIngredient.CODEC.fieldOf("fluid").forGetter { it.fluid },
                RecipeCodecs.CRAFTING_RESULT.fieldOf("result").forGetter { it.result }
            ).apply(builder, ::FluidBrewingRecipe)
        }
    }

    class Serializer : RecipeSerializer<FluidBrewingRecipe> {
        override fun codec(): Codec<FluidBrewingRecipe> = CODEC

        override fun read(buf: PacketByteBuf): FluidBrewingRecipe {
            val first = Ingredient.fromPacket(buf)
            val second = Ingredient.fromPacket(buf)
            val fluid = FluidIngredient.load(buf)
            val output = buf.readItemStack()
            return FluidBrewingRecipe(first, second, fluid, output)
        }

        override fun write(buf: PacketByteBuf, recipe: FluidBrewingRecipe) {
            recipe.firstIngredient.write(buf)
            recipe.secondIngredient.write(buf)
            recipe.fluid.write(buf)
            buf.writeItemStack(recipe.result)
        }
    }
}

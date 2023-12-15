package juuxel.adorn.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import juuxel.adorn.block.entity.BrewerBlockEntity.Companion.LEFT_INGREDIENT_SLOT
import juuxel.adorn.block.entity.BrewerBlockEntity.Companion.RIGHT_INGREDIENT_SLOT
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.world.World

class ItemBrewingRecipe(
    val firstIngredient: Ingredient,
    val secondIngredient: Ingredient,
    val result: ItemStack
) : BrewingRecipe {
    override fun matches(inventory: BrewerInventory, world: World): Boolean {
        fun matches(index: Int, ingredient: Ingredient) =
            ingredient.test(inventory.getStack(index))

        return (matches(LEFT_INGREDIENT_SLOT, firstIngredient) && matches(RIGHT_INGREDIENT_SLOT, secondIngredient)) ||
            (matches(RIGHT_INGREDIENT_SLOT, firstIngredient) && matches(LEFT_INGREDIENT_SLOT, secondIngredient))
    }

    override fun craft(inventory: BrewerInventory, registryManager: DynamicRegistryManager): ItemStack = result.copy()
    override fun fits(width: Int, height: Int): Boolean = true
    override fun getResult(registryManager: DynamicRegistryManager): ItemStack = result
    override fun getSerializer(): RecipeSerializer<*> = AdornRecipes.BREWING_SERIALIZER

    companion object {
        val CODEC: Codec<ItemBrewingRecipe> = RecordCodecBuilder.create { builder ->
            builder.group(
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("first_ingredient").forGetter { it.firstIngredient },
                Ingredient.ALLOW_EMPTY_CODEC.optionalFieldOf("second_ingredient", Ingredient.empty()).forGetter { it.secondIngredient },
                ItemStack.RECIPE_RESULT_CODEC.fieldOf("result").forGetter { it.result }
            ).apply(builder, ::ItemBrewingRecipe)
        }
    }

    class Serializer : RecipeSerializer<ItemBrewingRecipe> {
        override fun codec(): Codec<ItemBrewingRecipe> = CODEC

        override fun read(buf: PacketByteBuf): ItemBrewingRecipe {
            val first = Ingredient.fromPacket(buf)
            val second = Ingredient.fromPacket(buf)
            val output = buf.readItemStack()
            return ItemBrewingRecipe(first, second, output)
        }

        override fun write(buf: PacketByteBuf, recipe: ItemBrewingRecipe) {
            recipe.firstIngredient.write(buf)
            recipe.secondIngredient.write(buf)
            buf.writeItemStack(recipe.result)
        }
    }
}

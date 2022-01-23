package juuxel.adorn.compat.rei

import juuxel.adorn.item.AdornItems
import juuxel.adorn.recipe.BrewingRecipe
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.display.Display
import me.shedaniel.rei.api.common.display.DisplaySerializer
import me.shedaniel.rei.api.common.entry.EntryIngredient
import me.shedaniel.rei.api.common.entry.EntryStack
import me.shedaniel.rei.api.common.util.EntryIngredients
import me.shedaniel.rei.api.common.util.EntryStacks
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement

class BrewerDisplay(
    val input: EntryIngredient,
    val first: EntryIngredient,
    val second: EntryIngredient,
    val result: EntryStack<*>
) : Display {
    constructor(recipe: BrewingRecipe) : this(
        EntryIngredients.of(AdornItems.MUG),
        EntryIngredients.ofIngredient(recipe.firstIngredient),
        EntryIngredients.ofIngredient(recipe.secondIngredient),
        EntryStacks.of(recipe.result)
    )

    override fun getInputEntries(): List<EntryIngredient> =
        listOf(input, first, second)

    override fun getOutputEntries(): List<EntryIngredient> =
        listOf(EntryIngredient.of(result))

    override fun getCategoryIdentifier(): CategoryIdentifier<*> =
        AdornReiServer.BREWER

    object Serializer : DisplaySerializer<BrewerDisplay> {
        override fun save(tag: NbtCompound, display: BrewerDisplay): NbtCompound = tag.apply {
            put("Input", display.input.save())
            put("FirstIngredient", display.first.save())
            put("SecondIngredient", display.second.save())
            put("Result", display.result.save())
        }

        override fun read(tag: NbtCompound): BrewerDisplay {
            val input = EntryIngredient.read(tag.getList("Input", NbtElement.COMPOUND_TYPE.toInt()))
            val first = EntryIngredient.read(tag.getList("FirstIngredient", NbtElement.COMPOUND_TYPE.toInt()))
            val second = EntryIngredient.read(tag.getList("SecondIngredient", NbtElement.COMPOUND_TYPE.toInt()))
            val result = EntryStack.read(tag.getCompound("Result"))
            return BrewerDisplay(input, first, second, result)
        }
    }
}

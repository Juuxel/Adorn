package juuxel.adorn.compat.extrapieces.item

import com.google.common.collect.Multimap
import com.google.common.collect.MultimapBuilder
import com.shnupbups.extrapieces.core.PieceSet
import com.shnupbups.extrapieces.core.PieceType
import com.shnupbups.extrapieces.recipe.ShapedPieceRecipe
import com.swordglowsblue.artifice.api.ArtificeResourcePack
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

class AdornPieceRecipe(output: PieceType, count: Int, vararg pattern: String) :
    ShapedPieceRecipe(output, count, *pattern) {
    private val ingredientItems: Multimap<Char, Identifier> = MultimapBuilder.hashKeys().hashSetValues().build()
    private val ingredientTags: Multimap<Char, Identifier> = MultimapBuilder.hashKeys().hashSetValues().build()

    fun addToKey(key: Char, item: Item) = apply {
        ingredientItems.put(key, Registry.ITEM.getId(item))
    }

    fun addTagToKey(key: Char, id: Identifier) = apply {
        ingredientTags.put(key, id)
    }

    override fun addToKey(c: Char, type: PieceType): AdornPieceRecipe {
        super.addToKey(c, type)
        return this
    }

    override fun add(data: ArtificeResourcePack.ServerResourcePackBuilder, id: Identifier, set: PieceSet) {
        data.addShapedRecipe(id) { recipe ->
            val output = getOutput(set)
            recipe.result(Registry.BLOCK.getId(output), count)
            recipe.group(Registry.BLOCK.getId(output))
            recipe.pattern(*pattern)
            for (key in getKey(set).keys) {
                recipe.ingredientItem(key, Registry.ITEM.getId(getFromKey(set, key).asItem()))
            }
            for (key in ingredientItems.keySet() + ingredientTags.keySet()) {
                val items = ingredientItems.asMap().getOrElse(key) { emptyList() }
                val tags = ingredientTags.asMap().getOrElse(key) { emptyList() }
                recipe.multiIngredient(key) { builder ->
                    items.forEach { builder.item(it) }
                    tags.forEach { builder.tag(it) }
                }
            }
        }
    }
}

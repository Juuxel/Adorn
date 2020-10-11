package juuxel.adorn.lib

import juuxel.adorn.Adorn
import net.fabricmc.fabric.api.tag.TagRegistry
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier

object AdornTags {
    val CHAIRS = blockAndItem(Adorn.id("chairs"))
    val TABLES = blockAndItem(Adorn.id("tables"))
    val DRAWERS = blockAndItem(Adorn.id("drawers"))
    val KITCHEN_COUNTERS = blockAndItem(Adorn.id("kitchen_counters"))
    val KITCHEN_CUPBOARDS = blockAndItem(Adorn.id("kitchen_cupboards"))
    val KITCHEN_BLOCKS = blockAndItem(Adorn.id("kitchen_blocks"))
    val SOFAS = blockAndItem(Adorn.id("sofas"))
    val POSTS = blockAndItem(Adorn.id("posts"))
    val PLATFORMS = blockAndItem(Adorn.id("platforms"))
    val STEPS = blockAndItem(Adorn.id("steps"))
    val WOODEN_POSTS = blockAndItem(Adorn.id("wooden_posts"))
    val WOODEN_PLATFORMS = blockAndItem(Adorn.id("wooden_platforms"))
    val WOODEN_STEPS = blockAndItem(Adorn.id("wooden_steps"))
    val STONE_POSTS = blockAndItem(Adorn.id("stone_posts"))
    val STONE_PLATFORMS = blockAndItem(Adorn.id("stone_platforms"))
    val STONE_STEPS = blockAndItem(Adorn.id("stone_steps"))
    val SHELVES = blockAndItem(Adorn.id("shelves"))
    val WOODEN_SHELVES = blockAndItem(Adorn.id("wooden_shelves"))
    val CHIMNEYS = blockAndItem(Adorn.id("chimneys"))

    val STONE_RODS = item(Identifier("c", "stone_rods"))

    fun init() {}

    private fun block(id: Identifier): Tag.Identified<Block> =
        TagRegistry.block(id).toIdentified(id)

    private fun item(id: Identifier): Tag.Identified<Item> =
        TagRegistry.item(id).toIdentified(id)

    private fun blockAndItem(id: Identifier) = TagPair(
        block(id),
        item(id)
    )

    private fun <T> Tag<T>.toIdentified(id: Identifier): Tag.Identified<T> =
        if (this is Tag.Identified<T>) this
        else object : Tag<T> by this, Tag.Identified<T> {
            override fun getId() = id
        }

    data class TagPair(val block: Tag.Identified<Block>, val item: Tag.Identified<Item>)
}

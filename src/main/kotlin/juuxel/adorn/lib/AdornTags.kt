package juuxel.adorn.lib

import juuxel.adorn.Adorn
import net.fabricmc.fabric.api.tag.TagRegistry.block
import net.fabricmc.fabric.api.tag.TagRegistry.item
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

    val STONE_ROD = item(Identifier("c", "stone_rod"))

    fun init() {}

    private fun blockAndItem(id: Identifier) = TagPair(
        block(id),
        item(id)
    )

    data class TagPair(val block: Tag<Block>, val item: Tag<Item>)
}

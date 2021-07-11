package juuxel.adorn.lib

import juuxel.adorn.AdornCommon
import juuxel.adorn.platform.PlatformBridges
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier

object AdornTags {
    val CHAIRS = blockAndItem(AdornCommon.id("chairs"))
    val TABLES = blockAndItem(AdornCommon.id("tables"))
    val DRAWERS = blockAndItem(AdornCommon.id("drawers"))
    val BENCHES = blockAndItem(AdornCommon.id("benches"))
    val KITCHEN_COUNTERS = blockAndItem(AdornCommon.id("kitchen_counters"))
    val KITCHEN_CUPBOARDS = blockAndItem(AdornCommon.id("kitchen_cupboards"))
    val KITCHEN_BLOCKS = blockAndItem(AdornCommon.id("kitchen_blocks"))
    val SOFAS = blockAndItem(AdornCommon.id("sofas"))
    val POSTS = blockAndItem(AdornCommon.id("posts"))
    val PLATFORMS = blockAndItem(AdornCommon.id("platforms"))
    val STEPS = blockAndItem(AdornCommon.id("steps"))
    val WOODEN_POSTS = blockAndItem(AdornCommon.id("wooden_posts"))
    val WOODEN_PLATFORMS = blockAndItem(AdornCommon.id("wooden_platforms"))
    val WOODEN_STEPS = blockAndItem(AdornCommon.id("wooden_steps"))
    val STONE_POSTS = blockAndItem(AdornCommon.id("stone_posts"))
    val STONE_PLATFORMS = blockAndItem(AdornCommon.id("stone_platforms"))
    val STONE_STEPS = blockAndItem(AdornCommon.id("stone_steps"))
    val SHELVES = blockAndItem(AdornCommon.id("shelves"))
    val WOODEN_SHELVES = blockAndItem(AdornCommon.id("wooden_shelves"))
    val CHIMNEYS = blockAndItem(AdornCommon.id("chimneys"))

    val STONE_RODS = item(Identifier("c", "stone_rods"))

    fun init() {}

    private fun block(id: Identifier): Tag.Identified<Block> =
        PlatformBridges.tags.block(id).toIdentified(id)

    private fun item(id: Identifier): Tag.Identified<Item> =
        PlatformBridges.tags.item(id).toIdentified(id)

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

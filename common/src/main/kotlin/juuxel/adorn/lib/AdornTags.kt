package juuxel.adorn.lib

import juuxel.adorn.AdornCommon
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object AdornTags {
    @JvmField val CHAIRS = blockAndItem(AdornCommon.id("chairs"))
    @JvmField val TABLES = blockAndItem(AdornCommon.id("tables"))
    @JvmField val DRAWERS = blockAndItem(AdornCommon.id("drawers"))
    @JvmField val BENCHES = blockAndItem(AdornCommon.id("benches"))
    @JvmField val KITCHEN_COUNTERS = blockAndItem(AdornCommon.id("kitchen_counters"))
    @JvmField val KITCHEN_CUPBOARDS = blockAndItem(AdornCommon.id("kitchen_cupboards"))
    @JvmField val KITCHEN_BLOCKS = blockAndItem(AdornCommon.id("kitchen_blocks"))
    @JvmField val SOFAS = blockAndItem(AdornCommon.id("sofas"))
    @JvmField val POSTS = blockAndItem(AdornCommon.id("posts"))
    @JvmField val PLATFORMS = blockAndItem(AdornCommon.id("platforms"))
    @JvmField val STEPS = blockAndItem(AdornCommon.id("steps"))
    @JvmField val WOODEN_POSTS = blockAndItem(AdornCommon.id("wooden_posts"))
    @JvmField val WOODEN_PLATFORMS = blockAndItem(AdornCommon.id("wooden_platforms"))
    @JvmField val WOODEN_STEPS = blockAndItem(AdornCommon.id("wooden_steps"))
    @JvmField val STONE_POSTS = blockAndItem(AdornCommon.id("stone_posts"))
    @JvmField val STONE_PLATFORMS = blockAndItem(AdornCommon.id("stone_platforms"))
    @JvmField val STONE_STEPS = blockAndItem(AdornCommon.id("stone_steps"))
    @JvmField val SHELVES = blockAndItem(AdornCommon.id("shelves"))
    @JvmField val WOODEN_SHELVES = blockAndItem(AdornCommon.id("wooden_shelves"))
    @JvmField val CHIMNEYS = blockAndItem(AdornCommon.id("chimneys"))

    @JvmStatic
    fun init() {}

    private fun block(id: Identifier): TagKey<Block> =
        TagKey.of(Registry.BLOCK_KEY, id)

    private fun item(id: Identifier): TagKey<Item> =
        TagKey.of(Registry.ITEM_KEY, id)

    private fun blockAndItem(id: Identifier) = TagPair(
        block(id),
        item(id)
    )

    data class TagPair(val block: TagKey<Block>, val item: TagKey<Item>)
}

package juuxel.adorn.lib

import juuxel.adorn.AdornCommon
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object AdornTags {
    val CHAIRS = blockAndItem("chairs")
    val TABLES = blockAndItem("tables")
    val DRAWERS = blockAndItem("drawers")
    val BENCHES = blockAndItem("benches")
    val KITCHEN_COUNTERS = blockAndItem("kitchen_counters")
    val KITCHEN_CUPBOARDS = blockAndItem("kitchen_cupboards")
    val KITCHEN_SINKS = blockAndItem("kitchen_sinks")
    val KITCHEN_BLOCKS = blockAndItem("kitchen_blocks")
    val SOFAS = blockAndItem("sofas")
    val POSTS = blockAndItem("posts")
    val PLATFORMS = blockAndItem("platforms")
    val STEPS = blockAndItem("steps")
    val WOODEN_POSTS = blockAndItem("wooden_posts")
    val WOODEN_PLATFORMS = blockAndItem("wooden_platforms")
    val WOODEN_STEPS = blockAndItem("wooden_steps")
    val STONE_POSTS = blockAndItem("stone_posts")
    val STONE_PLATFORMS = blockAndItem("stone_platforms")
    val STONE_STEPS = blockAndItem("stone_steps")
    val SHELVES = blockAndItem("shelves")
    val WOODEN_SHELVES = blockAndItem("wooden_shelves")
    val CHIMNEYS = blockAndItem("chimneys")
    val CRATES = blockAndItem("crates")
    val COFFEE_TABLES = blockAndItem("coffee_tables")
    val TABLE_LAMPS = blockAndItem("table_lamps")
    val CANDLELIT_LANTERNS = blockAndItem("candlelit_lanterns")

    @JvmStatic
    fun init() {}

    private fun block(id: Identifier): TagKey<Block> =
        TagKey.of(Registry.BLOCK_KEY, id)

    private fun item(id: Identifier): TagKey<Item> =
        TagKey.of(Registry.ITEM_KEY, id)

    private fun blockAndItem(path: String): TagPair {
        val id = AdornCommon.id(path)
        return TagPair(block(id), item(id))
    }

    data class TagPair(val block: TagKey<Block>, val item: TagKey<Item>)
}

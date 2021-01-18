package juuxel.adorn.lib

import juuxel.adorn.Adorn
import net.minecraft.item.Item
import net.minecraft.tag.ItemTags
import net.minecraft.tag.Tag

object AdornItemTags {
    val CHAIRS = register("chairs")
    val TABLES = register("tables")
    val DRAWERS = register("drawers")
    val SOFAS = register("sofas")
    val WOODEN_POSTS = register("wooden_posts")
    val WOODEN_PLATFORMS = register("wooden_platforms")
    val WOODEN_STEPS = register("wooden_steps")
    val WOODEN_SHELVES = register("wooden_shelves")

    val BURN_TIMES: Map<Tag<Item>, Int> = mapOf(
        CHAIRS to 300,
        DRAWERS to 300,
        WOODEN_POSTS to 300,
        WOODEN_PLATFORMS to 300,
        WOODEN_STEPS to 300,
        WOODEN_SHELVES to 300,
        SOFAS to 150,
    )

    fun init() {}

    private fun register(id: String): Tag.Identified<Item> =
        ItemTags.createOptional(Adorn.id(id))
}

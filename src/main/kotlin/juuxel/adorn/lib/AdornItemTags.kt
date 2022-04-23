package juuxel.adorn.lib

import juuxel.adorn.Adorn
import net.minecraft.item.Item
import net.minecraft.tag.ItemTags
import net.minecraft.tag.Tag

object AdornItemTags {
    val CHAIRS = register("chairs")
    val TABLES = register("tables")
    val DRAWERS = register("drawers")
    val BENCHES = register("benches")
    val SOFAS = register("sofas")
    val WOODEN_POSTS = register("wooden_posts")
    val WOODEN_PLATFORMS = register("wooden_platforms")
    val WOODEN_STEPS = register("wooden_steps")
    val WOODEN_SHELVES = register("wooden_shelves")

    fun init() {}

    private fun register(id: String): Tag.Identified<Item> =
        ItemTags.createOptional(Adorn.id(id))
}

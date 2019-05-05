package juuxel.adorn.lib

import juuxel.adorn.Adorn
import net.fabricmc.fabric.api.tag.TagRegistry
import net.minecraft.util.Identifier

object ModTags {
    val CHAIRS = blockAndItem(Adorn.id("chairs"))
    val TABLES = blockAndItem(Adorn.id("tables"))
    val DRAWERS = blockAndItem(Adorn.id("drawers"))
    val KITCHEN_COUNTERS = blockAndItem(Adorn.id("kitchen_counters"))
    val KITCHEN_CUPBOARDS = blockAndItem(Adorn.id("kitchen_cupboards"))
    val KITCHEN_BLOCKS = blockAndItem(Adorn.id("kitchen_blocks"))
    val SOFAS = blockAndItem(Adorn.id("sofas"))

    fun init() {}

    private fun blockAndItem(id: Identifier) = Pair(
        TagRegistry.block(id),
        TagRegistry.item(id)
    )
}

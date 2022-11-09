package juuxel.adorn.datagen.tag

import juuxel.adorn.datagen.ColorMaterial
import juuxel.adorn.datagen.StoneMaterial
import juuxel.adorn.datagen.WoodMaterial

object TagEntryProviders {
    val BENCHES = wood("bench")
    val CHAIRS = wood("chair")
    val COFFEE_TABLES = wood("coffee_table")
    val DRAWERS = wood("drawer")
    val DYED_CANDLELIT_LANTERNS = wool("candlelit_lantern")
    val KITCHEN_COUNTERS = wood("kitchen_counter")
    val KITCHEN_CUPBOARDS = wood("kitchen_cupboard")
    val KITCHEN_SINKS = wood("kitchen_sink")
    val SOFAS = wool("sofa")
    val STONE_PLATFORMS = stone("platform")
    val STONE_POSTS = stone("post")
    val STONE_STEPS = stone("step")
    val TABLES = wood("table")
    val TABLE_LAMPS = wool("table_lamp")
    val WOODEN_PLATFORMS = wood("platform")
    val WOODEN_POSTS = wood("post")
    val WOODEN_SHELVES = wood("shelf")
    val WOODEN_STEPS = wood("step")
    val NON_FLAMMABLE_WOOD = TagEntryProvider.Filtered(
        TagEntryProvider.Multi(
            BENCHES,
            CHAIRS,
            COFFEE_TABLES,
            DRAWERS,
            KITCHEN_COUNTERS,
            KITCHEN_CUPBOARDS,
            KITCHEN_SINKS,
            TABLES,
            WOODEN_PLATFORMS,
            WOODEN_POSTS,
            WOODEN_SHELVES,
            WOODEN_STEPS,
        ),
        filter = { it is WoodMaterial && it.nonFlammable }
    )

    private fun wood(blockType: String): TagEntryProvider =
        TagEntryProvider.Filtered(
            TagEntryProvider.Simple(blockType),
            filter = { it is WoodMaterial }
        )

    private fun stone(blockType: String): TagEntryProvider =
        TagEntryProvider.Filtered(
            TagEntryProvider.Simple(blockType),
            filter = { it is StoneMaterial }
        )

    private fun wool(blockType: String): TagEntryProvider =
        TagEntryProvider.Filtered(
            TagEntryProvider.Simple(blockType),
            filter = { it is ColorMaterial }
        )
}

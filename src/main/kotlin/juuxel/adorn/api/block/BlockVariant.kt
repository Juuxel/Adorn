package juuxel.adorn.api.block

import net.minecraft.block.AbstractBlock
import net.minecraft.block.Blocks
import net.minecraft.util.DyeColor

interface BlockVariant {
    /**
     * The name of this variant. Must be a valid identifier path.
     */
    val name: String

    /**
     * Creates a *new* `AbstractBlock.Settings`.
     */
    fun createSettings(): AbstractBlock.Settings

    data class Wood(override val name: String) : BlockVariant {
        override fun createSettings() = AbstractBlock.Settings.copy(Blocks.OAK_FENCE)
    }

    data class Stone(override val name: String) : BlockVariant {
        override fun createSettings() = AbstractBlock.Settings.copy(Blocks.COBBLESTONE_WALL)
    }

    companion object {
        val WOOLS: Map<DyeColor, BlockVariant> = DyeColor.values().asSequence().associateWith {
            variant(it.asString()) { AbstractBlock.Settings.copy(Blocks.WHITE_WOOL) }
        }

        val IRON = variant("iron") { AbstractBlock.Settings.copy(Blocks.IRON_BARS) }
        val OAK = variant("oak") { AbstractBlock.Settings.copy(Blocks.OAK_PLANKS) }
        val SPRUCE = variant("spruce") { AbstractBlock.Settings.copy(Blocks.SPRUCE_PLANKS) }
        val BIRCH = variant("birch") { AbstractBlock.Settings.copy(Blocks.BIRCH_PLANKS) }
        val JUNGLE = variant("jungle") { AbstractBlock.Settings.copy(Blocks.JUNGLE_PLANKS) }
        val ACACIA = variant("acacia") { AbstractBlock.Settings.copy(Blocks.ACACIA_PLANKS) }
        val DARK_OAK = variant("dark_oak") { AbstractBlock.Settings.copy(Blocks.DARK_OAK_PLANKS) }
        val CRIMSON = variant("crimson") { AbstractBlock.Settings.copy(Blocks.CRIMSON_PLANKS) }
        val WARPED = variant("warped") { AbstractBlock.Settings.copy(Blocks.WARPED_PLANKS) }
        val STONE = variant("stone") { AbstractBlock.Settings.copy(Blocks.STONE) }
        val COBBLESTONE = variant("cobblestone") { AbstractBlock.Settings.copy(Blocks.COBBLESTONE) }
        val SANDSTONE = variant("sandstone") { AbstractBlock.Settings.copy(Blocks.SANDSTONE) }
        val DIORITE = variant("diorite") { AbstractBlock.Settings.copy(Blocks.DIORITE) }
        val ANDESITE = variant("andesite") { AbstractBlock.Settings.copy(Blocks.ANDESITE) }
        val GRANITE = variant("granite") { AbstractBlock.Settings.copy(Blocks.GRANITE) }
        val BRICK = variant("brick") { AbstractBlock.Settings.copy(Blocks.BRICKS) }
        val STONE_BRICK = variant("stone_brick") { AbstractBlock.Settings.copy(Blocks.STONE_BRICKS) }
        val RED_SANDSTONE = variant("red_sandstone") { AbstractBlock.Settings.copy(Blocks.RED_SANDSTONE) }
        val NETHER_BRICK = variant("nether_brick") { AbstractBlock.Settings.copy(Blocks.NETHER_BRICKS) }
        val BASALT = variant("basalt") { AbstractBlock.Settings.copy(Blocks.BASALT) }

        inline fun variant(name: String, crossinline settings: () -> AbstractBlock.Settings): BlockVariant =
            object : BlockVariant {
                override val name = name

                override fun createSettings() = settings()
            }

        fun wool(color: DyeColor): BlockVariant = WOOLS[color] ?: error("Could not find wool variant for color $color")
    }
}

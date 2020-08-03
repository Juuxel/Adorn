package juuxel.adorn.api.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
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

        val IRON = variant("iron", Blocks.IRON_BARS)
        val OAK = variant("oak", Blocks.OAK_PLANKS)
        val SPRUCE = variant("spruce", Blocks.SPRUCE_PLANKS)
        val BIRCH = variant("birch", Blocks.BIRCH_PLANKS)
        val JUNGLE = variant("jungle", Blocks.JUNGLE_PLANKS)
        val ACACIA = variant("acacia", Blocks.ACACIA_PLANKS)
        val DARK_OAK = variant("dark_oak", Blocks.DARK_OAK_PLANKS)
        val CRIMSON = variant("crimson", Blocks.CRIMSON_PLANKS)
        val WARPED = variant("warped", Blocks.WARPED_PLANKS)
        val STONE = variant("stone", Blocks.STONE)
        val COBBLESTONE = variant("cobblestone", Blocks.COBBLESTONE)
        val SANDSTONE = variant("sandstone", Blocks.SANDSTONE)
        val DIORITE = variant("diorite", Blocks.DIORITE)
        val ANDESITE = variant("andesite", Blocks.ANDESITE)
        val GRANITE = variant("granite", Blocks.GRANITE)
        val BRICK = variant("brick", Blocks.BRICKS)
        val STONE_BRICK = variant("stone_brick", Blocks.STONE_BRICKS)
        val RED_SANDSTONE = variant("red_sandstone", Blocks.RED_SANDSTONE)
        val NETHER_BRICK = variant("nether_brick", Blocks.NETHER_BRICKS)
        val BASALT = variant("basalt", Blocks.BASALT)
        val BLACKSTONE = variant("blackstone", Blocks.BLACKSTONE)
        val RED_NETHER_BRICK = variant("red_nether_brick", Blocks.RED_NETHER_BRICKS)
        val PRISMARINE = variant("prismarine", Blocks.PRISMARINE)
        val QUARTZ = variant("quartz", Blocks.QUARTZ_BLOCK)
        val END_STONE_BRICK = variant("end_stone_brick", Blocks.END_STONE_BRICKS)
        val PURPUR = variant("purpur", Blocks.PURPUR_BLOCK)
        val POLISHED_BLACKSTONE = variant("blackstone", Blocks.POLISHED_BLACKSTONE)
        val POLISHED_BLACKSTONE_BRICK = variant("red_nether_brick", Blocks.POLISHED_BLACKSTONE_BRICKS)
        val POLISHED_DIORITE = variant("polished_diorite", Blocks.POLISHED_DIORITE)
        val POLISHED_ANDESITE = variant("polished_andesite", Blocks.POLISHED_ANDESITE)
        val POLISHED_GRANITE = variant("polished_granite", Blocks.POLISHED_GRANITE)
        val PRISMARINE_BRICK = variant("prismarine_brick", Blocks.PRISMARINE_BRICKS)
        val DARK_PRISMARINE = variant("dark_prismarine", Blocks.DARK_PRISMARINE)
        val CUT_SANDSTONE = variant("cut_sandstone", Blocks.CUT_SANDSTONE)
        val SMOOTH_SANDSTONE = variant("smooth_sandstone", Blocks.SMOOTH_SANDSTONE)
        val CUT_RED_SANDSTONE = variant("cut_red_sandstone", Blocks.CUT_RED_SANDSTONE)
        val SMOOTH_RED_SANDSTONE = variant("smooth_red_sandstone", Blocks.SMOOTH_RED_SANDSTONE)
        val SMOOTH_STONE = variant("smooth_stone", Blocks.SMOOTH_STONE)
        val MOSSY_COBBLESTONE = variant("mossy_cobblestone", Blocks.MOSSY_COBBLESTONE)
        val MOSSY_STONE_BRICK = variant("mossy_stone_brick", Blocks.MOSSY_STONE_BRICKS)

        inline fun variant(name: String, base: Block): BlockVariant = variant(name) { FabricBlockSettings.copyOf(base) }

        inline fun variant(name: String, crossinline settings: () -> AbstractBlock.Settings): BlockVariant =
            object : BlockVariant {
                override val name = name

                override fun createSettings() = settings()
            }

        fun wool(color: DyeColor): BlockVariant = WOOLS[color] ?: error("Could not find wool variant for color $color")
    }
}

package juuxel.adorn.api.block

import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.util.DyeColor

interface BlockVariant {
    /**
     * The name of this variant. Must be a valid identifier path.
     */
    val name: String

    /**
     * Creates a *new* `Block.Settings`.
     */
    fun createSettings(): Block.Settings

    data class Wood(override val name: String) : BlockVariant {
        override fun createSettings() = Block.Settings.copy(Blocks.OAK_FENCE)
    }

    data class Stone(override val name: String) : BlockVariant {
        override fun createSettings() = Block.Settings.copy(Blocks.COBBLESTONE_WALL)
    }

    companion object {
        val WOOLS: Map<DyeColor, BlockVariant> = DyeColor.values().asSequence().associateWith {
            variant(it.asString()) { Block.Settings.copy(Blocks.WHITE_WOOL) }
        }

        val IRON = variant("iron") { Block.Settings.copy(Blocks.IRON_BARS) }
        val OAK = variant("oak") { Block.Settings.copy(Blocks.OAK_PLANKS) }
        val SPRUCE = variant("spruce") { Block.Settings.copy(Blocks.SPRUCE_PLANKS) }
        val BIRCH = variant("birch") { Block.Settings.copy(Blocks.BIRCH_PLANKS) }
        val JUNGLE = variant("jungle") { Block.Settings.copy(Blocks.JUNGLE_PLANKS) }
        val ACACIA = variant("acacia") { Block.Settings.copy(Blocks.ACACIA_PLANKS) }
        val DARK_OAK = variant("dark_oak") { Block.Settings.copy(Blocks.DARK_OAK_PLANKS) }
        val CRIMSON = variant("crimson") { Block.Settings.copy(Blocks.CRIMSON_PLANKS) }
        val WARPED = variant("warped") { Block.Settings.copy(Blocks.WARPED_PLANKS) }
        val STONE = variant("stone") { Block.Settings.copy(Blocks.STONE) }
        val COBBLESTONE = variant("cobblestone") { Block.Settings.copy(Blocks.COBBLESTONE) }
        val SANDSTONE = variant("sandstone") { Block.Settings.copy(Blocks.SANDSTONE) }
        val DIORITE = variant("diorite") { Block.Settings.copy(Blocks.DIORITE) }
        val ANDESITE = variant("andesite") { Block.Settings.copy(Blocks.ANDESITE) }
        val GRANITE = variant("granite") { Block.Settings.copy(Blocks.GRANITE) }
        val BRICK = variant("brick") { Block.Settings.copy(Blocks.BRICKS) }
        val STONE_BRICK = variant("stone_brick") { Block.Settings.copy(Blocks.STONE_BRICKS) }
        val RED_SANDSTONE = variant("red_sandstone") { Block.Settings.copy(Blocks.RED_SANDSTONE) }
        val NETHER_BRICK = variant("nether_brick") { Block.Settings.copy(Blocks.NETHER_BRICKS) }
        val BASALT = variant("basalt") { Block.Settings.copy(Blocks.BASALT) }

        inline fun variant(name: String, crossinline settings: () -> Block.Settings): BlockVariant =
            object : BlockVariant {
                override val name = name

                override fun createSettings() = settings()
            }

        fun wool(color: DyeColor): BlockVariant = WOOLS[color] ?: error("Could not find wool variant for color $color")
    }
}

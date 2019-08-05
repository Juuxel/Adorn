package juuxel.adorn.api.util

import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.util.DyeColor

interface BlockVariant {
    /**
     * The name of this variant. Must be a valid identifier path.
     */
    val variantName: String

    /**
     * Creates a *new* `Block.Settings`.
     */
    fun createSettings(): Block.Settings

    data class Wood(override val variantName: String) : BlockVariant {
        override fun createSettings() = Block.Settings.copy(Blocks.OAK_FENCE)
    }

    data class Stone(override val variantName: String) : BlockVariant {
        override fun createSettings() = Block.Settings.copy(Blocks.COBBLESTONE_WALL)
    }

    enum class VanillaStone(override val variantName: String) : BlockVariant {
        Stone("stone"), Cobblestone("cobblestone"), Sandstone("sandstone"),
        Diorite("diorite"), Andesite("andesite"), Granite("granite");

        override fun createSettings() = Block.Settings.copy(Blocks.COBBLESTONE_WALL)
    }

    companion object {
        val WOOLS: Map<DyeColor, BlockVariant> = DyeColor.values().asSequence().associateWith {
            variant(it.asString()) { Block.Settings.copy(Blocks.WHITE_WOOL) }
        }

        val IRON = variant("iron") { Block.Settings.copy(Blocks.IRON_BARS) }

        private inline fun variant(name: String, crossinline settings: () -> Block.Settings): BlockVariant =
            object : BlockVariant {
                override val variantName = name

                override fun createSettings() = settings()
            }
    }
}

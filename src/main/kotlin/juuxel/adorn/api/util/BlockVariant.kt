package juuxel.adorn.api.util

import net.minecraft.block.Block
import net.minecraft.block.Blocks

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

    enum class Stone(override val variantName: String) : BlockVariant {
        SmoothStone("stone"), Cobblestone("cobblestone"), Sandstone("sandstone"),
        Diorite("diorite"), Andesite("andesite"), Granite("granite");

        override fun createSettings() = Block.Settings.copy(Blocks.COBBLESTONE_WALL)
    }
}

package juuxel.adorn.util

import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.util.Identifier

interface BlockVariant {
    val id: Identifier
    val settings: Block.Settings

    data class Wood(override val id: Identifier) : BlockVariant {
        override val settings = Block.Settings.copy(Blocks.OAK_FENCE)
    }

    enum class Stone(contentName: String) : BlockVariant {
        SmoothStone("stone"), Cobblestone("cobblestone"), Sandstone("sandstone"),
        Diorite("diorite"), Andesite("andesite"), Granite("granite");

        override val id = Identifier("minecraft", contentName)
        override val settings = Block.Settings.copy(Blocks.COBBLESTONE_WALL)
    }
}

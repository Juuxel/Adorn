package juuxel.adorn.api.util

import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.util.Identifier
import org.apiguardian.api.API

@API(status = API.Status.MAINTAINED)
interface BlockVariant {
    @get:API(status = API.Status.DEPRECATED, since = "1.2.0")
    @Deprecated("Replaced with name", ReplaceWith("this.variantName"))
    val id: Identifier get() = Identifier(variantName)

    /**
     * The name of this variant. Must be a valid identifier path.
     */
    @get:API(status = API.Status.EXPERIMENTAL, since = "1.2.0")
    val variantName: String

    @get:API(status = API.Status.DEPRECATED, since = "1.2.0")
    @Deprecated("Replaced with createSettings()", ReplaceWith("this.createSettings()"))
    val settings: Block.Settings get() = createSettings()

    /**
     * Creates a *new* `Block.Settings`.
     */
    fun createSettings(): Block.Settings

    data class Wood(override val variantName: String) : BlockVariant {
        @Deprecated("", ReplaceWith("Wood(id.path)"))
        constructor(id: Identifier) : this(id.path)

        override fun createSettings() = Block.Settings.copy(Blocks.OAK_FENCE)
    }

    enum class Stone(override val variantName: String) : BlockVariant {
        SmoothStone("stone"), Cobblestone("cobblestone"), Sandstone("sandstone"),
        Diorite("diorite"), Andesite("andesite"), Granite("granite");

        override fun createSettings() = Block.Settings.copy(Blocks.COBBLESTONE_WALL)
    }
}

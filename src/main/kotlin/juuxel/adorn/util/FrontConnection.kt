package juuxel.adorn.util

import net.minecraft.util.StringRepresentable

/**
 * Note: sofas have left and right flipped
 */
enum class FrontConnection(private val value: String) : StringRepresentable {
    /**
     * No connection to the block in front.
     */
    None("none"),

    /**
     * The block will be connected to the block in front on its left side (looking from the connecting block).
     */
    Left("left"),

    /**
     * The block will be connected to the block in front on its right side (looking from the connecting block).
     */
    Right("right");

    override fun asString() = value
}

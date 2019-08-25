package juuxel.adorn.block

import net.minecraft.block.Block

/**
 * Can be added to a block to provide a description for the block item
 * when registered using [juuxel.polyester.registry.PolyesterRegistry.registerBlock] (the overloads that require a string).
 */
interface BlockWithDescription {
    val descriptionKey: String get() = (this as Block).translationKey + ".desc"
}

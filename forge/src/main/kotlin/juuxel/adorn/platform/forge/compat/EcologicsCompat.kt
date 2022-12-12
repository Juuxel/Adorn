package juuxel.adorn.platform.forge.compat

import juuxel.adorn.block.variant.BlockVariant
import juuxel.adorn.block.variant.BlockVariantSet

object EcologicsCompat : BlockVariantSet {
    override val woodVariants = listOf(
        "azalea",
        "flowering_azalea",
        "coconut",
        "walnut",
    ).map { BlockVariant.Wood("ecologics/$it") }
}

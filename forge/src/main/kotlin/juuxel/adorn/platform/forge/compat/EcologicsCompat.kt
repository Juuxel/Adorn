package juuxel.adorn.platform.forge.compat

import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.compat.BlockVariantSet

object EcologicsCompat : BlockVariantSet {
    override val woodVariants = listOf(
        "azalea",
        "flowering_azalea",
        "coconut",
        "walnut",
    ).map { BlockVariant.Wood("ecologics/$it") }
}

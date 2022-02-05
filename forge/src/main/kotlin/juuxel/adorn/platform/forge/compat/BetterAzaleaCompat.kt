package juuxel.adorn.platform.forge.compat

import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.compat.BlockVariantSet

object BetterAzaleaCompat : BlockVariantSet {
    override val woodVariants = listOf(
        "azalea",
        "flowering_azalea",
    ).map { BlockVariant.Wood("azalea/$it") }
}

package juuxel.adorn.compat.vanillaparts

import juuxel.adorn.compat.AdornCompat
import juuxel.adorn.part.creator
import juuxel.vanillaparts.part.CarpetPart
import juuxel.vanillaparts.part.VPartDefinitions

object AdornVanillaPartsCompat {
    fun init() {
        AdornCompat.Variables.carpetCreatorCreator = { color ->
            creator {
                CarpetPart(VPartDefinitions.CARPET_PARTS[color], it, color)
            }
        }
    }
}

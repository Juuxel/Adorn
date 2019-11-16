package juuxel.adorn.compat

import alexiil.mc.lib.multipart.api.MultipartContainer
import juuxel.adorn.compat.terrestria.AdornTerrestriaCompat
import juuxel.adorn.compat.traverse.AdornTraverseCompat
import juuxel.adorn.compat.vanillaparts.AdornVanillaPartsCompat
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.DyeColor

object AdornCompat {
    fun init() {
        ifModLoaded("traverse") { AdornTraverseCompat.init() }
        ifModLoaded("terrestria") { AdornTerrestriaCompat.init() }
        ifModLoaded("vanilla_parts") { AdornVanillaPartsCompat.init() }
    }

    private inline fun ifModLoaded(mod: String, fn: () -> Unit) {
        if (FabricLoader.getInstance().isModLoaded(mod)) {
            fn()
        }
    }

    // Variables that can be changed by compat handlers
    object Variables {
        internal var carpetCreatorCreator: (DyeColor) -> MultipartContainer.MultipartCreator? = { null }
    }
}

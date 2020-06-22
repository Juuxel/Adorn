package juuxel.adorn.compat

import juuxel.adorn.compat.terrestria.AdornTerrestriaCompat
import juuxel.adorn.compat.towelette.AdornToweletteCompat
import juuxel.adorn.compat.traverse.AdornTraverseCompat
import juuxel.adorn.compat.woodsandmires.AdornWamCompat
import net.fabricmc.loader.api.FabricLoader

object AdornCompat {
    fun init() {
        ifModLoaded("traverse") { AdornTraverseCompat.init() }
        ifModLoaded("terrestria") { AdornTerrestriaCompat.init() }
        ifModLoaded("towelette") { AdornToweletteCompat.init() }
        ifModLoaded("woods_and_mires") { AdornWamCompat.init() }
    }

    private inline fun ifModLoaded(mod: String, fn: () -> Unit) {
        if (FabricLoader.getInstance().isModLoaded(mod)) {
            fn()
        }
    }
}

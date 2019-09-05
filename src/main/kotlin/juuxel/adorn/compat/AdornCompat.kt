package juuxel.adorn.compat

import juuxel.adorn.compat.terrestria.AdornTerrestriaCompat
import juuxel.adorn.compat.traverse.AdornTraverseCompat
import net.fabricmc.loader.api.FabricLoader

object AdornCompat {
    fun init() {
        ifModLoaded("traverse") { AdornTraverseCompat.init() }
        ifModLoaded("terrestria") { AdornTerrestriaCompat.init() }
    }

    private inline fun ifModLoaded(mod: String, fn: () -> Unit) {
        if (FabricLoader.getInstance().isModLoaded(mod)) {
            fn()
        }
    }
}

package juuxel.adorn.lib

import juuxel.adorn.compat.traverse.AdornTraverseCompat
import net.fabricmc.loader.api.FabricLoader

object AdornCompat {
    fun init() {
        ifMod("traverse") { AdornTraverseCompat.init() }
    }

    private inline fun ifMod(mod: String, fn: () -> Unit) {
        if (FabricLoader.getInstance().isModLoaded(mod)) {
            fn()
        }
    }
}

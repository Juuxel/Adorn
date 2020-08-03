package juuxel.adorn.compat

import juuxel.adorn.compat.terrestria.AdornTerrestriaCompat
import juuxel.adorn.compat.towelette.AdornToweletteCompat
import juuxel.adorn.compat.traverse.AdornTraverseCompat
import juuxel.adorn.compat.woodsandmires.AdornWamCompat
import juuxel.adorn.config.ConfigManager
import net.fabricmc.loader.api.FabricLoader

object AdornCompat {
    fun init() {
        ifModLoaded("traverse") { AdornTraverseCompat.init() }
        ifModLoaded("terrestria") { AdornTerrestriaCompat.init() }
        ifModLoaded("towelette") { AdornToweletteCompat.init() }
        ifModLoaded("woods_and_mires") { AdornWamCompat.init() }
    }

    fun isCompatEnabled(mod: String): Boolean {
        val compatMap = ConfigManager.CONFIG.compat

        if (mod !in compatMap) {
            compatMap[mod] = true
            ConfigManager.save()
            return true
        }

        return compatMap[mod]!!
    }

    private inline fun ifModLoaded(mod: String, fn: () -> Unit) {
        if (isCompatEnabled(mod) && FabricLoader.getInstance().isModLoaded(mod)) {
            fn()
        }
    }
}

package juuxel.adorn.compat

import juuxel.adorn.config.ConfigManager
import net.fabricmc.loader.api.FabricLoader

object Compat {
    fun init() {
        val mods = mapOf(
            "byg" to BygCompat::init,
            "terrestria" to TerrestriaCompat::init,
            "towelette" to ToweletteCompat::init,
            "traverse" to TraverseCompat::init,
            "woods_and_mires" to WamCompat::init,
            "biomemakeover" to BiomeMakeoverCompat::init
        )

        for ((mod, fn) in mods) {
            ifModLoaded(mod, fn)
        }
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

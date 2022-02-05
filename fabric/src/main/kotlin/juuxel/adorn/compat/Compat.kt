package juuxel.adorn.compat

import juuxel.adorn.platform.fabric.ConfigManagerImpl
import net.fabricmc.loader.api.FabricLoader

object Compat {
    fun init() {
        val mods = mapOf(
            "byg" to { CompatBlocks.addVariants(BygCompat) },
            "terrestria" to { CompatBlocks.addVariants(TerrestriaCompat) },
            "towelette" to ToweletteCompat::init,
            "traverse" to { CompatBlocks.addVariants(TraverseCompat) },
            "woods_and_mires" to { CompatBlocks.addVariants(WamCompat) },
            "biomemakeover" to { CompatBlocks.addVariants(BiomeMakeoverCompat) },
            "cinderscapes" to { CompatBlocks.addVariants(CinderscapesCompat) },
            "promenade" to { CompatBlocks.addVariants(PromenadeCompat) },
            "techreborn" to { CompatBlocks.addVariants(TechRebornCompat) }
        )

        for ((mod, fn) in mods) {
            ifModLoaded(mod, fn)
        }

        CompatBlocks.register()
    }

    fun isCompatEnabled(mod: String): Boolean {
        val compatMap = ConfigManagerImpl.config.compat

        if (mod !in compatMap) {
            compatMap[mod] = true
            ConfigManagerImpl.save()
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

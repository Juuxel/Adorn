package juuxel.adorn.compat

import juuxel.adorn.block.variant.BlockVariantSets
import juuxel.adorn.config.ConfigManager
import net.fabricmc.loader.api.FabricLoader

object Compat {
    fun init() {
        val mods = mapOf(
            "byg" to { BlockVariantSets.add(BygCompat) },
            "terrestria" to { BlockVariantSets.add(TerrestriaCompat) },
            "towelette" to ToweletteCompat::init,
            "traverse" to { BlockVariantSets.add(TraverseCompat) },
            "woods_and_mires" to { BlockVariantSets.add(WamCompat) },
            "biomemakeover" to { BlockVariantSets.add(BiomeMakeoverCompat) },
            "cinderscapes" to { BlockVariantSets.add(CinderscapesCompat) },
            "promenade" to { BlockVariantSets.add(PromenadeCompat) },
            "techreborn" to { BlockVariantSets.add(TechRebornCompat) },
            "blockus" to { BlockVariantSets.add(BlockusCompat) },
        )

        for ((mod, fn) in mods) {
            ifModLoaded(mod, fn)
        }
    }

    fun isCompatEnabled(mod: String): Boolean {
        val compatMap = ConfigManager.config().compat

        if (mod !in compatMap) {
            compatMap[mod] = true
            ConfigManager.INSTANCE.save()
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

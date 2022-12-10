package juuxel.adorn.platform.forge.compat

import juuxel.adorn.compat.CompatBlocks
import juuxel.adorn.config.ConfigManager
import juuxel.adorn.platform.forge.registrar.ForgeRegistrar
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.ModList

object Compat {
    fun init(modBus: IEventBus) {
        val mods = mapOf(
            "biomemakeover" to { CompatBlocks.addVariants(BiomeMakeoverCompat) },
            "biomesoplenty" to { CompatBlocks.addVariants(BiomesOPlentyCompat) },
            "byg" to { CompatBlocks.addVariants(BygCompat) },
            "ecologics" to { CompatBlocks.addVariants(EcologicsCompat) },
            "architects_palette" to { CompatBlocks.addVariants(ArchitectsPaletteCompat) },
        )

        for ((mod, fn) in mods) {
            ifModLoaded(mod, fn)
        }

        CompatBlocks.register()
        (CompatBlocks.blocks as ForgeRegistrar<*>).hook(modBus)
        (CompatBlocks.items as ForgeRegistrar<*>).hook(modBus)
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
        if (isCompatEnabled(mod) && ModList.get().isLoaded(mod)) {
            fn()
        }
    }
}

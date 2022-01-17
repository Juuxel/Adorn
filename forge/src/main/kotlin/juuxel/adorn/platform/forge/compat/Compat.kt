package juuxel.adorn.platform.forge.compat

import juuxel.adorn.compat.CompatBlocks
import juuxel.adorn.platform.forge.ConfigManagerImpl
import juuxel.adorn.platform.forge.RegistrarImpl
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.ModList

object Compat {
    fun init(modBus: IEventBus) {
        val mods = mapOf(
            "biomesoplenty" to { CompatBlocks.addVariants(BiomesOPlentyCompat) },
            "byg" to { CompatBlocks.addVariants(BygCompat) },
        )

        for ((mod, fn) in mods) {
            ifModLoaded(mod, fn)
        }

        CompatBlocks.register()
        (CompatBlocks.blocks as RegistrarImpl<*>).register.register(modBus)
        (CompatBlocks.items as RegistrarImpl<*>).register.register(modBus)
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
        if (isCompatEnabled(mod) && ModList.get().isLoaded(mod)) {
            fn()
        }
    }
}

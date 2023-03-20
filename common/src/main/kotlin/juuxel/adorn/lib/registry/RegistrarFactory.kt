package juuxel.adorn.lib.registry

import juuxel.adorn.util.InlineServices
import juuxel.adorn.util.loadService
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey

interface RegistrarFactory {
    fun <T> create(registryKey: RegistryKey<Registry<T>>): Registrar<T>

    @InlineServices
    companion object {
        private val instance: RegistrarFactory by lazy { loadService() }
        fun get() = instance
    }
}

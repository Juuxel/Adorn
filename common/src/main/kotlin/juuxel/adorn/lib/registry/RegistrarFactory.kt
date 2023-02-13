package juuxel.adorn.lib.registry

import juuxel.adorn.util.InlineServices
import juuxel.adorn.util.loadService
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier

interface RegistrarFactory {
    fun <T> create(registryKey: RegistryKey<Registry<T>>): Registrar<T>
    fun <T> newRegistry(type: Class<T>, registryId: Identifier, defaultId: Identifier): Registrar<T>

    @InlineServices
    companion object {
        private val instance: RegistrarFactory by lazy { loadService() }
        fun get() = instance
    }
}

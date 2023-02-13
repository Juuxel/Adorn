package juuxel.adorn.lib.registry

import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey

interface RegistrarFactory {
    fun <T> create(registryKey: RegistryKey<Registry<T>>): Registrar<T>
}

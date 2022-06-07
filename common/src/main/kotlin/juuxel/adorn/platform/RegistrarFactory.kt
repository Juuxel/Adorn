package juuxel.adorn.platform

import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey

interface RegistrarFactory {
    fun <T> create(registryKey: RegistryKey<Registry<T>>): Registrar<T>
}

package juuxel.adorn.platform.fabric

import juuxel.adorn.lib.registry.Registrar
import juuxel.adorn.lib.registry.RegistrarFactory
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey

class FabricRegistrarFactory : RegistrarFactory {
    @Suppress("UNCHECKED_CAST")
    override fun <T> create(registryKey: RegistryKey<Registry<T>>): Registrar<T> {
        val registry = (Registries.REGISTRIES as Registry<Registry<T>>).get(registryKey)
            ?: throw IllegalArgumentException("No registry found for key $registryKey")
        return RegistrarImpl(registry)
    }
}

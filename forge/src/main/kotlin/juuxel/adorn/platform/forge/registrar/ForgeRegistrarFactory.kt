package juuxel.adorn.platform.forge.registrar

import juuxel.adorn.lib.registry.Registrar
import juuxel.adorn.lib.registry.RegistrarFactory
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey

class ForgeRegistrarFactory : RegistrarFactory {
    override fun <T> create(registryKey: RegistryKey<Registry<T>>): Registrar<T> =
        DeferredRegistrar(registryKey)
}

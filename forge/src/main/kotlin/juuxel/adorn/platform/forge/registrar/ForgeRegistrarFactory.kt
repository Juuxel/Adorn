package juuxel.adorn.platform.forge.registrar

import juuxel.adorn.lib.registry.Registrar
import juuxel.adorn.lib.registry.RegistrarFactory
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier

class ForgeRegistrarFactory : RegistrarFactory {
    override fun <T> create(registryKey: RegistryKey<Registry<T>>): Registrar<T> =
        DeferredRegistrar(registryKey)

    override fun <T> newRegistry(type: Class<T>, registryId: Identifier, defaultId: Identifier): Registrar<T> =
        CreatingDeferredRegistrar(RegistryKey.ofRegistry(registryId), defaultId)
}

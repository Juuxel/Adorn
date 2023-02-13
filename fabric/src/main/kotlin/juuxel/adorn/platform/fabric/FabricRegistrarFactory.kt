package juuxel.adorn.platform.fabric

import juuxel.adorn.lib.registry.Registrar
import juuxel.adorn.lib.registry.RegistrarFactory
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier

class FabricRegistrarFactory : RegistrarFactory {
    @Suppress("UNCHECKED_CAST")
    override fun <T> create(registryKey: RegistryKey<Registry<T>>): Registrar<T> {
        val registry = (Registries.REGISTRIES as Registry<Registry<T>>).get(registryKey)
            ?: throw IllegalArgumentException("No registry found for key $registryKey")
        return RegistrarImpl(registry)
    }

    override fun <T> newRegistry(type: Class<T>, registryId: Identifier, defaultId: Identifier): Registrar<T> {
        val registry = FabricRegistryBuilder.createDefaulted(type, registryId, defaultId)
            .buildAndRegister()
        return RegistrarImpl(registry)
    }
}

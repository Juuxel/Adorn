package juuxel.adorn.platform.forge

import juuxel.adorn.lib.registry.Registrar
import juuxel.adorn.lib.registry.RegistrarFactory
import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.platform.forge.registrar.DeferredRegistrar
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey

class PlatformBridgesImpl : PlatformBridges {
    override val blockEntities = BlockEntityBridgeForge
    override val blockFactory = BlockFactoryImpl
    override val entities = EntityBridgeImpl
    override val menus = MenuBridgeImpl
    override val network = NetworkBridgeImpl
    override val registrarFactory: RegistrarFactory = object : RegistrarFactory {
        override fun <T> create(registryKey: RegistryKey<Registry<T>>): Registrar<T> =
            DeferredRegistrar(registryKey)
    }
    override val resources = ResourceBridgeImpl
}

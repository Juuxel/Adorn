package juuxel.adorn.platform.forge

import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.platform.Registrar
import juuxel.adorn.platform.RegistrarFactory
import juuxel.adorn.platform.forge.registrar.DeferredRegistrar
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey

class PlatformBridgesImpl : PlatformBridges {
    override val blockEntities = BlockEntityBridgeForge
    override val blockFactory = BlockFactoryImpl
    override val configManager = ConfigManagerImpl
    override val entities = EntityBridgeImpl
    override val fluids = FluidBridgeForge
    override val items = ItemBridgeImpl
    override val menus = MenuBridgeImpl
    override val network = NetworkBridgeImpl
    override val registrarFactory: RegistrarFactory = object : RegistrarFactory {
        override fun <T> create(registryKey: RegistryKey<Registry<T>>): Registrar<T> =
            DeferredRegistrar(registryKey)
    }
    override val resources = ResourceBridgeImpl
}

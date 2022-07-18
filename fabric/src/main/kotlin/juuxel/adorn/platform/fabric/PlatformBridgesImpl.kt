package juuxel.adorn.platform.fabric

import juuxel.adorn.platform.BlockFactory
import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.platform.Registrar
import juuxel.adorn.platform.RegistrarFactory
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey

class PlatformBridgesImpl : PlatformBridges {
    override val blockEntities = BlockEntityBridgeFabric
    override val blockFactory = BlockFactory
    override val entities = EntityBridgeImpl
    override val items = ItemBridgeImpl
    override val menus = MenuBridgeImpl
    override val network = NetworkBridgeImpl
    override val registrarFactory: RegistrarFactory = object : RegistrarFactory {
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(registryKey: RegistryKey<Registry<T>>): Registrar<T> =
            RegistrarImpl(
                (Registry.REGISTRIES as Registry<Registry<T>>).get(registryKey)
                    ?: throw IllegalArgumentException("No registry found for key $registryKey")
            )
    }
    override val resources = ResourceBridgeImpl
}

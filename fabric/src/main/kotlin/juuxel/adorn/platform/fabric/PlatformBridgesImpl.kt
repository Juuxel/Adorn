package juuxel.adorn.platform.fabric

import juuxel.adorn.lib.registry.Registrar
import juuxel.adorn.lib.registry.RegistrarFactory
import juuxel.adorn.platform.BlockFactory
import juuxel.adorn.platform.PlatformBridges
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey

class PlatformBridgesImpl : PlatformBridges {
    override val blockEntities = BlockEntityBridgeFabric
    override val blockFactory = BlockFactory
    override val entities = EntityBridgeImpl
    override val menus = MenuBridgeImpl
    override val network = NetworkBridgeImpl
    override val registrarFactory: RegistrarFactory = object : RegistrarFactory {
        @Suppress("UNCHECKED_CAST")
        override fun <T> create(registryKey: RegistryKey<Registry<T>>): Registrar<T> =
            RegistrarImpl(
                (Registries.REGISTRIES as Registry<Registry<T>>).get(registryKey)
                    ?: throw IllegalArgumentException("No registry found for key $registryKey")
            )
    }
    override val resources = ResourceBridgeImpl
}

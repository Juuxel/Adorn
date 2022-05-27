package juuxel.adorn.platform

import juuxel.adorn.config.ConfigManager
import java.util.ServiceLoader

interface PlatformBridges {
    val blockEntities: BlockEntityBridge
    val blockFactory: BlockFactory
    val configManager: ConfigManager
    val entities: EntityBridge
    val fluids: FluidBridge
    val fluidRendering: FluidRenderingBridge
    val items: ItemBridge
    val menus: MenuBridge
    val network: NetworkBridge
    val registrarFactory: RegistrarFactory
    val resources: ResourceBridge

    companion object {
        private val instance: PlatformBridges by lazy {
            val serviceLoader = ServiceLoader.load(PlatformBridges::class.java)
            serviceLoader.findFirst().orElseThrow { RuntimeException("Could not find Adorn platform implementation!") }
        }

        fun get() = instance

        inline val blockEntities get() = get().blockEntities
        inline val blockFactory get() = get().blockFactory
        inline val configManager get() = get().configManager
        inline val entities get() = get().entities
        inline val items get() = get().items
        inline val menus get() = get().menus
        inline val network get() = get().network
        inline val registrarFactory get() = get().registrarFactory
        inline val resources get() = get().resources
    }
}

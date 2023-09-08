package juuxel.adorn.platform

import juuxel.adorn.util.InlineServices
import juuxel.adorn.util.loadService

interface PlatformBridges {
    val blockEntities: BlockEntityBridge
    val blockFactory: BlockFactory
    val entities: EntityBridge
    val items: ItemBridge
    val menus: MenuBridge
    val network: NetworkBridge
    val registrarFactory: RegistrarFactory
    val resources: ResourceBridge

    @InlineServices
    companion object {
        private val instance: PlatformBridges by lazy { loadService() }
        fun get() = instance

        inline val blockEntities get() = get().blockEntities
        inline val blockFactory get() = get().blockFactory
        inline val entities get() = get().entities
        inline val items get() = get().items
        inline val menus get() = get().menus
        inline val network get() = get().network
        inline val registrarFactory get() = get().registrarFactory
        inline val resources get() = get().resources
    }
}

package juuxel.adorn.platform

import juuxel.adorn.util.InlineServices
import juuxel.adorn.util.loadService

interface PlatformBridges {
    val blockEntities: BlockEntityBridge
    val blockFactory: BlockFactory
    val entities: EntityBridge
    val menus: MenuBridge
    val network: NetworkBridge
    val resources: ResourceBridge

    @InlineServices
    companion object {
        private val instance: PlatformBridges by lazy { loadService() }
        fun get() = instance

        inline val blockEntities get() = get().blockEntities
        inline val blockFactory get() = get().blockFactory
        inline val entities get() = get().entities
        inline val menus get() = get().menus
        inline val network get() = get().network
        inline val resources get() = get().resources
    }
}

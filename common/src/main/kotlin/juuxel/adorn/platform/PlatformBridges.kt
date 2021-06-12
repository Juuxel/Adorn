package juuxel.adorn.platform

import dev.architectury.injectables.annotations.ExpectPlatform
import juuxel.adorn.config.CommonConfig

@ExpectPlatform
fun PlatformBridges.Companion.get(): PlatformBridges =
    throw AssertionError("Untransformed ExpectPlatform")

interface PlatformBridges {
    val blocks: BlockBridge
    val blockEntities: BlockEntityDescriptors
    val config: CommonConfig
    val entities: EntityBridge
    val items: ItemBridge
    val menus: MenuBridge
    val network: NetworkBridge
    val registrarFactory: RegistrarFactory
    val tags: TagBridge

    companion object {
        inline val blocks get() = get().blocks
        inline val blockEntities get() = get().blockEntities
        inline val config get() = get().config
        inline val entities get() = get().entities
        inline val items get() = get().items
        inline val menus get() = get().menus
        inline val network get() = get().network
        inline val registrarFactory get() = get().registrarFactory
        inline val tags get() = get().tags
    }
}

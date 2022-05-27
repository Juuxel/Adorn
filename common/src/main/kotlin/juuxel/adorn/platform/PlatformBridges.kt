package juuxel.adorn.platform

import dev.architectury.injectables.annotations.ExpectPlatform
import juuxel.adorn.config.ConfigManager

@ExpectPlatform
fun PlatformBridges.Companion.get(): PlatformBridges =
    throw AssertionError("Untransformed ExpectPlatform")

interface PlatformBridges {
    val blockEntities: BlockEntityBridge
    val blockFactory: BlockFactory
    val configManager: ConfigManager
    val entities: EntityBridge
    val fluids: FluidBridge
    val items: ItemBridge
    val menus: MenuBridge
    val network: NetworkBridge
    val registrarFactory: RegistrarFactory
    val resources: ResourceBridge

    companion object {
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

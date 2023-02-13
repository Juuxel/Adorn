package juuxel.adorn.platform.forge

import juuxel.adorn.platform.PlatformBridges

class PlatformBridgesImpl : PlatformBridges {
    override val blockEntities = BlockEntityBridgeForge
    override val blockFactory = BlockFactoryImpl
    override val entities = EntityBridgeImpl
    override val menus = MenuBridgeImpl
    override val network = NetworkBridgeImpl
    override val resources = ResourceBridgeImpl
}

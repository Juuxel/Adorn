package juuxel.adorn.platform.fabric

import juuxel.adorn.platform.BlockFactory
import juuxel.adorn.platform.PlatformBridges

class PlatformBridgesImpl : PlatformBridges {
    override val blockEntities = BlockEntityBridgeFabric
    override val blockFactory = BlockFactory
    override val entities = EntityBridgeImpl
    override val menus = MenuBridgeImpl
    override val network = NetworkBridgeImpl
    override val resources = ResourceBridgeImpl
}

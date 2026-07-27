package juuxel.adorn.platform;

import juuxel.adorn.block.BlockFactoryNeo;
import juuxel.adorn.block.entity.BlockEntityBridgeNeo;
import juuxel.adorn.networking.NetworkBridgeNeo;

public final class PlatformBridgesNeo implements PlatformBridges {
    @Override
    public BlockEntityBridge getBlockEntities() {
        return BlockEntityBridgeNeo.INSTANCE;
    }

    @Override
    public BlockFactory getBlockFactory() {
        return BlockFactoryNeo.INSTANCE;
    }

    @Override
    public MenuBridge getMenus() {
        return MenuBridgeNeo.INSTANCE;
    }

    @Override
    public NetworkBridge getNetwork() {
        return NetworkBridgeNeo.INSTANCE;
    }
}

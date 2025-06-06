package juuxel.adorn.platform.fabric;

import juuxel.adorn.platform.BlockEntityBridge;
import juuxel.adorn.platform.BlockFactory;
import juuxel.adorn.platform.MenuBridge;
import juuxel.adorn.platform.NetworkBridge;
import juuxel.adorn.platform.PlatformBridges;
import juuxel.adorn.platform.ResourceBridge;

public final class PlatformBridgesImpl implements PlatformBridges {
    @Override
    public BlockEntityBridge getBlockEntities() {
        return BlockEntityBridgeFabric.INSTANCE;
    }

    @Override
    public BlockFactory getBlockFactory() {
        return BlockFactory.DEFAULT;
    }

    @Override
    public MenuBridge getMenus() {
        return MenuBridgeImpl.INSTANCE;
    }

    @Override
    public NetworkBridge getNetwork() {
        return NetworkBridgeImpl.INSTANCE;
    }

    @Override
    public ResourceBridge getResources() {
        return ResourceBridgeImpl.INSTANCE;
    }
}

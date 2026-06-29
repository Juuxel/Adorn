package juuxel.adorn.platform.fabric;

import juuxel.adorn.platform.ModBridge;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;

public final class ModBridgeFabric implements ModBridge {
    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public String getModName(String modId) {
        return FabricLoader.getInstance()
            .getModContainer(modId)
            .map(ModContainer::getMetadata)
            .map(ModMetadata::getName)
            .orElse(modId);
    }
}

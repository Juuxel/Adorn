package juuxel.adorn.platform.fabric;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.platform.ModBridge;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;

public final class ModBridgeFabric implements ModBridge {
    private final ModContainer modContainer = FabricLoader.getInstance()
        .getModContainer(AdornCommon.NAMESPACE)
        .orElseThrow();

    @Override
    public byte @Nullable [] readModFile(String path) throws IOException {
        var p = modContainer.findPath(path).orElse(null);
        return p != null ? Files.readAllBytes(p) : null;
    }

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

package juuxel.adorn.platform.fabric;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.platform.ModContentBridge;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;

public final class ModContentBridgeFabric implements ModContentBridge {
    private final ModContainer modContainer = FabricLoader.getInstance()
        .getModContainer(AdornCommon.NAMESPACE)
        .orElseThrow();

    @Override
    public byte @Nullable [] readModFile(String path) throws IOException {
        var p = modContainer.findPath(path).orElse(null);
        return p != null ? Files.readAllBytes(p) : null;
    }
}

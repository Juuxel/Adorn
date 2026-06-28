package juuxel.adorn.platform.neo;

import juuxel.adorn.platform.ModContentBridge;
import net.neoforged.fml.ModContainer;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ModContentBridgeNeo implements ModContentBridge {
    public static ModContainer modContainer;

    @Override
    public byte @Nullable [] readModFile(String path) throws IOException {
        Path p = modContainer.getModInfo()
            .getOwningFile()
            .getFile()
            .findResource(path);

        if (Files.exists(p)) {
            return Files.readAllBytes(p);
        }

        return null;
    }
}

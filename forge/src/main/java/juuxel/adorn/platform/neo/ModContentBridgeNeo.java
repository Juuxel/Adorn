package juuxel.adorn.platform.neo;

import juuxel.adorn.platform.ModContentBridge;
import net.neoforged.fml.ModContainer;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public final class ModContentBridgeNeo implements ModContentBridge {
    public static ModContainer modContainer;

    @Override
    public byte @Nullable [] readModFile(String path) throws IOException {
        return modContainer.getModInfo()
            .getOwningFile()
            .getFile()
            .getContents()
            .readFile(path);
    }
}

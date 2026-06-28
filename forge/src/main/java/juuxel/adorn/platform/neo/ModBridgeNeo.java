package juuxel.adorn.platform.neo;

import juuxel.adorn.platform.ModBridge;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModInfo;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public final class ModBridgeNeo implements ModBridge {
    public static ModContainer modContainer;

    @Override
    public byte @Nullable [] readModFile(String path) throws IOException {
        return modContainer.getModInfo()
            .getOwningFile()
            .getFile()
            .getContents()
            .readFile(path);
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public String getModName(String modId) {
        return ModList.get()
            .getModContainerById(modId)
            .map(ModContainer::getModInfo)
            .map(IModInfo::getDisplayName)
            .orElse(modId);
    }
}

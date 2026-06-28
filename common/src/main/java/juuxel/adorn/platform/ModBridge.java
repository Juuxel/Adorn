package juuxel.adorn.platform;

import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public interface ModBridge {
    byte @Nullable [] readModFile(String path) throws IOException;
    boolean isModLoaded(String modId);
    String getModName(String modId);

    @InlineServices.Getter
    static ModBridge get() {
        return Services.load(ModBridge.class);
    }
}

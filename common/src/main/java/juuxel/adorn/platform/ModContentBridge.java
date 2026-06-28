package juuxel.adorn.platform;

import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public interface ModContentBridge {
    byte @Nullable [] readModFile(String path) throws IOException;

    @InlineServices.Getter
    static ModContentBridge get() {
        return Services.load(ModContentBridge.class);
    }
}

package juuxel.adorn.platform;

import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;

public interface MappingBridge {
    String remapToRuntime(String className);

    @InlineServices.Getter
    static MappingBridge get() {
        return Services.load(MappingBridge.class);
    }
}

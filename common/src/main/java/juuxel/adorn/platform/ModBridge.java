package juuxel.adorn.platform;

import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;

public interface ModBridge {
    boolean isModLoaded(String modId);
    String getModName(String modId);

    @InlineServices.Getter
    static ModBridge get() {
        return Services.load(ModBridge.class);
    }
}

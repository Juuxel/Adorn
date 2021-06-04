package juuxel.adorn.platform.fabric;

import juuxel.adorn.config.CommonConfig;
import juuxel.adorn.config.ConfigManager;

public final class ConfigBridgeImpl {
    public static CommonConfig get() {
        return ConfigManager.INSTANCE.getConfig();
    }
}

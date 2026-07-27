package juuxel.adorn.platform;

import juuxel.adorn.config.ConfigManager;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public final class ConfigManagerNeo extends ConfigManager {
    @Override
    protected Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}

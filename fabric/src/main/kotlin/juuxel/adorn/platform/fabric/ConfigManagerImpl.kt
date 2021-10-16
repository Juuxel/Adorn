package juuxel.adorn.platform.fabric

import juuxel.adorn.config.ConfigManager
import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Path

object ConfigManagerImpl : ConfigManager() {
    override val configDirectory: Path = FabricLoader.getInstance().configDir
}

package juuxel.adorn.platform.fabric

import juuxel.adorn.config.CommonConfig
import juuxel.adorn.config.ConfigManager

object ConfigBridgeImpl {
    @JvmStatic
    fun get(): CommonConfig = ConfigManager.CONFIG
}

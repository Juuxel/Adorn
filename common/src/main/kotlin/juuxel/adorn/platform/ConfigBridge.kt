package juuxel.adorn.platform

import dev.architectury.injectables.annotations.ExpectPlatform
import juuxel.adorn.config.CommonConfig

object ConfigBridge {
    @JvmStatic
    @ExpectPlatform
    fun get(): CommonConfig = expected
}

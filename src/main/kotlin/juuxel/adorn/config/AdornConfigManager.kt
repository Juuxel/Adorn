package juuxel.adorn.config

import me.sargunvohra.mcmods.autoconfig1.AutoConfig
import me.sargunvohra.mcmods.autoconfig1.serializer.JanksonConfigSerializer

object AdornConfigManager {
    @get:JvmName("getConfig")
    val CONFIG: AdornConfig by lazy {
        AutoConfig.getConfigHolder(AdornConfig::class.java).config
    }

    fun init() {
        AutoConfig.register(AdornConfig::class.java, ::JanksonConfigSerializer)
    }
}
package juuxel.adorn.config

import blue.endless.jankson.Comment
import io.github.cottonmc.cotton.config.ConfigManager
import io.github.cottonmc.cotton.config.annotations.ConfigFile

@ConfigFile(name = "Adorn")
class AdornConfig {
    @Comment("If true, you can sit on tables.")
    @JvmField
    var sittingOnTables: Boolean = false

    companion object {
        val INSTANCE by lazy {
            ConfigManager.loadConfig(AdornConfig::class.java)
        }

        fun init() {}
    }
}

package juuxel.adorn.config

import io.github.cottonmc.cotton.config.ConfigManager
import io.github.cottonmc.cotton.config.annotations.ConfigFile
import io.github.cottonmc.repackage.blue.endless.jankson.Comment

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

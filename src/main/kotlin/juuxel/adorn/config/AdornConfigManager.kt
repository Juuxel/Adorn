package juuxel.adorn.config

import blue.endless.jankson.Jankson
import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Files

object AdornConfigManager {
    private val JANKSON = Jankson.builder().build()
    private val CONFIG_PATH = FabricLoader.getInstance().configDirectory.toPath().resolve("Adorn.json5")

    @get:JvmName("getConfig")
    val CONFIG: AdornConfig by lazy {
        if (Files.notExists(CONFIG_PATH)) {
            save(AdornConfig())
        }

        JANKSON.fromJson(Files.readAllLines(CONFIG_PATH).joinToString("\n"), AdornConfig::class.java)
    }

    fun init() {
        // Initialize the config
        CONFIG
    }

    fun save() = save(CONFIG)

    private fun save(config: AdornConfig) {
        Files.write(CONFIG_PATH, JANKSON.toJson(config).toJson().lines())
    }
}

package juuxel.adorn.config

import blue.endless.jankson.Jankson
import blue.endless.jankson.JsonObject
import net.fabricmc.loader.api.FabricLoader
import org.apache.logging.log4j.LogManager
import java.nio.file.Files

object AdornConfigManager {
    private val JANKSON = Jankson.builder().build()
    private val DEFAULT = JANKSON.toJson(AdornConfig()) as JsonObject
    private val CONFIG_PATH = FabricLoader.getInstance().configDirectory.toPath().resolve("Adorn.json5")
    private val LOGGER = LogManager.getLogger()

    @get:JvmName("getConfig")
    val CONFIG: AdornConfig by lazy {
        if (Files.notExists(CONFIG_PATH)) {
            save(AdornConfig())
        }

        try {
            val obj = JANKSON.load(Files.readAllLines(CONFIG_PATH).joinToString("\n"))
            val config = JANKSON.fromJsonCarefully(obj, AdornConfig::class.java)

            if (isMissingKeys(obj, DEFAULT)) {
                LOGGER.info("[Adorn] Upgrading config...")
                save(config)
            }

            config
        } catch (e: Exception) {
            throw RuntimeException("Failed to load Adorn config file!", e)
        }
    }

    fun init() {
        // Initialize the config
        CONFIG
    }

    fun save() = save(CONFIG)

    private fun save(config: AdornConfig) {
        Files.write(CONFIG_PATH, JANKSON.toJson(config).toJson(true, true).lines())
    }

    private fun isMissingKeys(config: JsonObject, defaults: JsonObject): Boolean {
        for ((key, value) in defaults) {
            if (!config.containsKey(key)) return true

            if (value is JsonObject && isMissingKeys(config.get(JsonObject::class.java, key) ?: return true, value)) {
                return true
            }
        }

        return false
    }
}

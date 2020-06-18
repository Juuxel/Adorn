package juuxel.adorn.config

import blue.endless.jankson.Jankson
import blue.endless.jankson.JsonObject
import blue.endless.jankson.api.DeserializationException
import net.fabricmc.loader.api.FabricLoader
import org.apache.logging.log4j.LogManager
import java.nio.file.Files

object ConfigManager {
    private val JANKSON = Jankson.builder().build()
    private val DEFAULT = JANKSON.toJson(Config()) as JsonObject
    private val CONFIG_PATH = FabricLoader.getInstance().configDirectory.toPath().resolve("Adorn.json5")
    private val LOGGER = LogManager.getLogger()

    @get:JvmName("getConfig")
    val CONFIG: Config by lazy {
        if (Files.notExists(CONFIG_PATH)) {
            save(Config())
        }

        try {
            val obj = JANKSON.load(Files.readAllLines(CONFIG_PATH).joinToString("\n"))
            val config = try {
                JANKSON.fromJsonCarefully(obj, Config::class.java)
            } catch (e: DeserializationException) {
                // Try deserializing carelessly and throw the exception if it returns null
                JANKSON.fromJson(obj, Config::class.java) ?: throw e
            }

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

    private fun save(config: Config) {
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

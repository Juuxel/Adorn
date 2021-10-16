package juuxel.adorn.config

import blue.endless.jankson.Jankson
import blue.endless.jankson.JsonObject
import blue.endless.jankson.api.DeserializationException
import org.apache.logging.log4j.LogManager
import java.nio.file.Files
import java.nio.file.Path

abstract class ConfigManager {
    protected abstract val configDirectory: Path
    private val configPath: Path by lazy { configDirectory.resolve("Adorn.sjon5") }
    private var saveScheduled: Boolean = false
    private var finalized: Boolean = false

    @get:JvmName("getConfig")
    val config: Config by lazy {
        if (Files.notExists(configPath)) {
            save(Config())
        }

        try {
            val obj = JANKSON.load(Files.readAllLines(configPath).joinToString("\n"))
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
        config
    }

    fun save() {
        if (finalized) {
            save(config)
        } else {
            saveScheduled = true
        }
    }

    fun finalize() {
        finalized = true
        if (saveScheduled) {
            save()
        }
    }

    private fun save(config: Config) {
        Files.write(configPath, JANKSON.toJson(config).toJson(true, true).lines())
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

    companion object {
        private val JANKSON = Jankson.builder().build()
        private val DEFAULT = JANKSON.toJson(Config()) as JsonObject
        private val LOGGER = LogManager.getLogger()
    }
}

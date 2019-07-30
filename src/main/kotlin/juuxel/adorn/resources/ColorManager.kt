package juuxel.adorn.resources

import com.google.gson.*
import io.github.cottonmc.cotton.gui.widget.WLabel
import juuxel.adorn.Adorn
import juuxel.adorn.util.color
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
import java.lang.reflect.Type

object ColorManager : SimpleSynchronousResourceReloadListener {
    private val LOGGER = LogManager.getLogger()
    private val GSON = GsonBuilder().registerTypeAdapter(ColorPair::class.java, ColorPair).create()
    private val ID = Adorn.id("color_manager")
    private const val PREFIX = "color_palettes"
    private const val SUFFIX_LENGTH = ".json".length
    private val COLOR_REGEX = Regex("#(?:[0-9A-Fa-f]{2})?[0-9A-Fa-f]{6}")
    private val map: MutableMap<Identifier, ColorPalette> = HashMap()

    override fun getFabricId() = ID

    override fun apply(manager: ResourceManager) {
        map.clear()
        val ids = manager.findResources(PREFIX) { it.endsWith(".json") }
        for (id in ids) {
            val scheme = HashMap<Identifier, ColorPair>()

            for (resource in manager.getAllResources(id)) {
                resource.inputStream.use { input ->
                    input.reader().use { reader ->
                        try {
                            val json = GSON.fromJson(reader, JsonObject::class.java)
                            for ((key, value) in json.entrySet()) {
                                val keyId = Identifier.tryParse(key)

                                if (keyId == null) {
                                    LOGGER.warn(
                                        "[Adorn] Invalid key '{}' in color palette {} - must be a valid identifier",
                                        key,
                                        resource.id
                                    )
                                    continue
                                }

                                scheme[keyId] = GSON.fromJson(value, ColorPair::class.java)
                            }
                        } catch (e: Exception) {
                            LOGGER.warn("[Adorn] Exception thrown while reading color palette in {}", resource.id, e)
                        }
                    }
                }
            }

            // id without prefix (adorn_colors/) and suffix (.json)
            val newId = Identifier(id.namespace, id.path.substring(PREFIX.length + 1, id.path.length - SUFFIX_LENGTH))
            map[newId] = ColorPalette(scheme)
        }
    }

    private fun parseHexColor(str: String): Int {
        require(str.matches(COLOR_REGEX)) {
            "Color must be a hex color beginning with '#' - found '$str'"
        }

        val colorStr = str.substring(1)
        return when (val len = colorStr.length) {
            6 -> color(colorStr.toInt(16))
            8 -> colorStr.toInt(16)
            else -> error("Invalid color length: $len")
        }
    }

    fun getColors(id: Identifier): ColorPalette =
        map[id] ?: throw IllegalArgumentException("Unknown palette $id")

    data class ColorPair(val bg: Int, val fg: Int = WLabel.DEFAULT_TEXT_COLOR) {
        companion object : JsonDeserializer<ColorPair> {
            override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?) =
                when (json) {
                    is JsonObject -> {
                        if (json.has("fg")) {
                            ColorPair(
                                bg = parseHexColor(json.get("bg").asString),
                                fg = parseHexColor(json.get("fg").asString)
                            )
                        } else {
                            ColorPair(bg = parseHexColor(json.get("bg").asString))
                        }
                    }

                    is JsonPrimitive -> ColorPair(bg = parseHexColor(json.asString))

                    else -> throw IllegalArgumentException("Invalid color value: $json")
                }
        }
    }
}

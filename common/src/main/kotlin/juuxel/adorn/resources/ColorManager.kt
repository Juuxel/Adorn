package juuxel.adorn.resources

import blue.endless.jankson.Jankson
import blue.endless.jankson.JsonElement
import blue.endless.jankson.JsonObject
import blue.endless.jankson.JsonPrimitive
import juuxel.adorn.AdornCommon
import juuxel.adorn.util.Colors
import juuxel.adorn.util.color
import juuxel.adorn.util.logger
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.SinglePreparationResourceReloader
import net.minecraft.util.Identifier
import net.minecraft.util.profiler.Profiler

open class ColorManager : SinglePreparationResourceReloader<Map<Identifier, List<JsonObject>>>() {
    private val jankson = Jankson.builder().build()
    private val map: MutableMap<Identifier, ColorPalette> = HashMap()

    override fun prepare(manager: ResourceManager, profiler: Profiler): Map<Identifier, List<JsonObject>> {
        val ids = manager.findAllResources(PREFIX) { it.path.endsWith(".json5") }
        return ids.mapValues { (_, resources) ->
            resources.map { resource ->
                resource.inputStream.use { input ->
                    jankson.load(input)
                }
            }
        }
    }

    override fun apply(map: Map<Identifier, List<JsonObject>>, manager: ResourceManager, profiler: Profiler) {
        for ((id, jsons) in map) {
            val scheme = HashMap<Identifier, ColorPair>()

            for (json in jsons) {
                for ((key, value) in json) {
                    val keyId = Identifier.tryParse(key)

                    if (keyId == null) {
                        LOGGER.warn("[Adorn] Invalid key '{}' in color palette {} - must be a valid identifier", key, id)
                        continue
                    }

                    scheme[keyId] = ColorPair.fromJson(value)
                }
            }

            // id without prefix (adorn_colors/) and suffix (.json)
            val newId = Identifier(id.namespace, id.path.substring(PREFIX.length + 1, id.path.length - SUFFIX_LENGTH))
            this.map[newId] = ColorPalette(scheme)
        }
    }

    fun getColors(id: Identifier): ColorPalette =
        map[id] ?: map[FALLBACK] ?: throw IllegalStateException("Could not find fallback palette!")

    companion object {
        private val LOGGER = logger()
        private val FALLBACK = AdornCommon.id("fallback")
        private const val PREFIX = "adorn/color_palettes"
        private const val SUFFIX_LENGTH = ".json5".length
        private val COLOR_REGEX = Regex("#(?:[0-9A-Fa-f]{2})?[0-9A-Fa-f]{6}")

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
    }

    data class ColorPair(val bg: Int, val fg: Int = Colors.SCREEN_TEXT) {
        companion object {
            private fun JsonElement?.asString() = (this as JsonPrimitive).asString()

            fun fromJson(json: JsonElement) =
                when (json) {
                    is JsonObject -> {
                        if (json.containsKey("fg")) {
                            ColorPair(bg = parseHexColor(json["bg"].asString()), fg = parseHexColor(json["fg"].asString()))
                        } else {
                            ColorPair(bg = parseHexColor(json["bg"].asString()))
                        }
                    }

                    is JsonPrimitive -> ColorPair(bg = parseHexColor(json.asString()))

                    else -> throw IllegalArgumentException("Invalid color value: $json")
                }
        }
    }
}

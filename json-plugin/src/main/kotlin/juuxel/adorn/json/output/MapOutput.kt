package juuxel.adorn.json.output

import io.github.cottonmc.jsonfactory.output.Json

// This is evil and must *never* end up in json-factory
class MapOutput(private val map: Map<*, *>) : Json {
    override fun toJsonString() = Json.toJson(map)
}

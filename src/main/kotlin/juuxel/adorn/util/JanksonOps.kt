package juuxel.adorn.util

import blue.endless.jankson.*
import com.mojang.datafixers.DSL
import com.mojang.datafixers.types.DynamicOps
import com.mojang.datafixers.types.Type
import java.util.Optional
import java.util.stream.Stream

// TODO: Remove in 1.15
object JanksonOps : DynamicOps<JsonElement> {
    override fun empty(): JsonElement = JsonNull.INSTANCE

    override fun getType(input: JsonElement): Type<*> = when (input) {
        is JsonObject -> DSL.compoundList(DSL.remainderType(), DSL.remainderType())
        is JsonArray -> DSL.list(DSL.remainderType())
        is JsonNull -> DSL.nilType()
        is JsonPrimitive -> {
            when (val value = input.value) {
                is String -> DSL.string()
                is Boolean -> DSL.bool()
                is Byte -> DSL.byteType()
                is Short -> DSL.shortType()
                is Int -> DSL.intType()
                is Long -> DSL.longType()
                is Float -> DSL.floatType()
                is Double -> DSL.doubleType()
                else -> throw IllegalArgumentException(
                    "JsonPrimitive '$input' has an unknown value type: ${value::class.qualifiedName}"
                )
            }
        }
        else -> throw IllegalArgumentException(
            "JsonElement '$input' has an unknown type: ${input::class.qualifiedName}"
        )
    }

    override fun getNumberValue(input: JsonElement): Optional<Number> {
        if (input is JsonPrimitive) {
            val value = input.value
            if (value is Number) return Optional.of(value)
            else if (value is Boolean) return Optional.of(if (value) 1 else 0)
        }
        return Optional.empty()
    }

    override fun createNumeric(i: Number) = JsonPrimitive(i)

    override fun createBoolean(value: Boolean) = JsonPrimitive(value)

    override fun getStringValue(input: JsonElement): Optional<String> =
        if (input is JsonPrimitive && input.value is String) Optional.of(input.value as String)
        else Optional.empty()

    override fun createString(value: String): JsonElement = JsonPrimitive(value)

    override fun mergeInto(input: JsonElement, value: JsonElement): JsonElement {
        if (value is JsonNull) return value
        require(input !is JsonNull) { "mergeInto called with null input." }

        if (input is JsonObject) {
            if (value is JsonObject) {
                val result = JsonObject()
                result.putAll(input)
                result.putAll(value)
                return result
            }

            return input
        } else if (input is JsonArray) {
            val result = JsonArray()
            result.addAll(input)
            result.add(value)
            return result
        }

        return input
    }

    override fun mergeInto(input: JsonElement, key: JsonElement, value: JsonElement): JsonElement {
        val output = when (input) {
            is JsonNull -> JsonObject()
            is JsonObject -> JsonObject().also { it.putAll(input) }
            else -> return input
        }

        output[(key as JsonPrimitive).asString()] = value
        return output
    }

    override fun merge(first: JsonElement, second: JsonElement): JsonElement {
        return when {
            first is JsonNull -> second
            second is JsonNull -> first

            first is JsonObject && second is JsonObject -> JsonObject().also { result ->
                result.putAll(first)
                result.putAll(second)
            }

            first is JsonArray && second is JsonArray -> JsonArray().also { result ->
                result.addAll(first)
                result.addAll(second)
            }

            else -> throw IllegalArgumentException("Could not merge $first and $second")
        }
    }

    override fun getMapValues(input: JsonElement): Optional<Map<JsonElement, JsonElement>> =
        if (input is JsonObject)
            Optional.of(input.mapKeys<String, JsonElement, JsonElement> { (key, _) -> JsonPrimitive(key) })
        else
            Optional.empty()

    override fun createMap(map: Map<JsonElement, JsonElement>): JsonElement =
        JsonObject().also { result ->
            for ((key, value) in map) {
                result[(key as JsonPrimitive).asString()] = value
            }
        }

    override fun getStream(input: JsonElement): Optional<Stream<JsonElement>> =
        if (input is JsonArray) Optional.of(input.stream())
        else Optional.empty()

    override fun createList(input: Stream<JsonElement>): JsonElement =
        JsonArray().also { result -> input.forEach { result.add(it) } }

    override fun remove(input: JsonElement, key: String): JsonElement {
        if (input is JsonObject) {
            val result = JsonObject()
            input.asSequence().filter { it.key != key }.forEach { (key, value) -> result[key] = value }
            return result
        }

        return input
    }

    override fun toString(): String {
        return "JSON"
    }
}

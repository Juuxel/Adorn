package juuxel.adorn.util

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.Dynamic
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.JsonOps
import net.minecraft.text.Text

object MoreCodecs {
    val TEXT: Codec<Text> = object : Codec<Text> {
        override fun <T> encode(input: Text, ops: DynamicOps<T>, prefix: T): DataResult<T> {
            val json = Text.Serializer.toJsonTree(input)
            val converted = Dynamic.convert(JsonOps.INSTANCE, ops, json)
            return ops.mergeToPrimitive(prefix, converted)
        }

        override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<Text, T>> {
            val json = Dynamic.convert(ops, JsonOps.INSTANCE, input)
            val text = Text.Serializer.fromJson(json)

            return if (text != null) {
                DataResult.success(Pair.of(text, ops.empty()))
            } else {
                DataResult.error { "Could not decode text $json" }
            }
        }

        override fun toString(): String = "Text"
    }
}

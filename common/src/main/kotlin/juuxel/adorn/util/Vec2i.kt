package juuxel.adorn.util

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import java.util.stream.IntStream

data class Vec2i(val x: Int, val y: Int) {
    companion object {
        private fun <T> mismatchedComponentCountResult(): DataResult<T> =
            DataResult.error { "Vec2i must have exactly two int components" }

        val CODEC: Codec<Vec2i> = object : Codec<Vec2i> {
            override fun <T> encode(input: Vec2i, ops: DynamicOps<T>, prefix: T): DataResult<T> =
                ops.mergeToPrimitive(prefix, ops.createIntList(IntStream.of(input.x, input.y)))

            override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<Vec2i, T>> =
                ops.getIntStream(input).flatMap {
                    val iter = it.iterator()

                    if (!iter.hasNext()) return@flatMap mismatchedComponentCountResult()
                    val x = iter.nextInt()
                    if (!iter.hasNext()) return@flatMap mismatchedComponentCountResult()
                    val y = iter.nextInt()
                    if (iter.hasNext()) return@flatMap mismatchedComponentCountResult()

                    DataResult.success(Pair.of(Vec2i(x, y), ops.empty()))
                }

            override fun toString() = "Vec2i"
        }
    }
}

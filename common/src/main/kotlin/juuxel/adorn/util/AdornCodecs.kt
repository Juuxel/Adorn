package juuxel.adorn.util

import com.mojang.datafixers.kinds.App
import com.mojang.datafixers.kinds.ListBox
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import org.joml.Vector3d
import java.util.stream.Stream

object AdornCodecs {
    val VECTOR_3D: Codec<Vector3d> = object : Codec<Vector3d> {
        override fun <T> encode(input: Vector3d, ops: DynamicOps<T>, prefix: T): DataResult<T> =
            ops.mergeToList(prefix, ops.createList(Stream.of(input.x, input.y, input.z).map { ops.createDouble(it) }))

        override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<Vector3d, T>> =
            ops.getStream(input).flatMap { stream ->
                val ts = stream.map<App<DataResult.Mu, Number>> { ops.getNumberValue(it) }.toList()
                DataResult.unbox(ListBox.flip(DataResult.instance(), ts))
            }.flatMap { numbers ->
                if (numbers.size != 3) {
                    return@flatMap DataResult.error("Expected 3 numbers for 3D vector, found ${numbers.size}")
                }

                val vector = Vector3d(numbers[0].toDouble(), numbers[1].toDouble(), numbers[2].toDouble())
                DataResult.success(Pair.of(vector, ops.empty()))
            }
    }
}

package juuxel.adorn.util

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.Dynamic
import com.mojang.serialization.JsonOps
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtOps
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import java.util.UUID

private val LOGGER = logger()

fun NbtCompound.putText(name: String, textComponent: Text) =
    put(
        name,
        Dynamic.convert(
            JsonOps.INSTANCE,
            NbtOps.INSTANCE,
            Text.Serializer.toJsonTree(textComponent)
        )
    )

fun NbtCompound.getText(name: String): Text? {
    val tag = get(name) ?: return null

    return Text.Serializer.fromJson(
        Dynamic.convert(
            NbtOps.INSTANCE,
            JsonOps.INSTANCE,
            tag
        )
    )
}

fun NbtCompound.containsOldUuid(key: String): Boolean =
    contains("${key}Most") && contains("${key}Least")

fun NbtCompound.getOldUuid(key: String): UUID =
    UUID(getLong("${key}Most"), getLong("${key}Least"))

fun NbtCompound.getBlockPos(key: String): BlockPos {
    val (x, y, z) = getIntArray(key)
    return BlockPos(x, y, z)
}

fun NbtCompound.putBlockPos(key: String, pos: BlockPos) {
    putIntArray(key, intArrayOf(pos.x, pos.y, pos.z))
}

fun NbtCompound.getCompoundOrNull(key: String): NbtCompound? =
    if (contains(key, NbtElement.COMPOUND_TYPE.toInt())) {
        getCompound(key)
    } else {
        null
    }

fun <T> NbtCompound.getWithCodec(key: String, codec: Codec<T>): DataResult<T> {
    if (contains(key)) {
        return codec.decode(NbtOps.INSTANCE, get(key)).map { it.first }
    }

    return DataResult.error("NBT does not contain key $key")
}

fun <T> NbtCompound.putWithCodec(key: String, t: T, codec: Codec<T>) {
    val element = codec.encodeStart(NbtOps.INSTANCE, t).resultOrPartial {
        LOGGER.error("[Adorn] Could not write '{}' {} using {}", key, t, codec)
    }.orElse(null) ?: return
    put(key, element)
}

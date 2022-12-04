package juuxel.adorn.util

import com.mojang.serialization.Dynamic
import com.mojang.serialization.JsonOps
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtOps
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import java.util.UUID

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

package juuxel.adorn.util

import com.mojang.serialization.Dynamic
import com.mojang.serialization.JsonOps
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.text.Text
import java.util.UUID

fun CompoundTag.putText(name: String, textComponent: Text) =
    put(
        name,
        Dynamic.convert(
            JsonOps.INSTANCE,
            NbtOps.INSTANCE,
            Text.Serializer.toJsonTree(textComponent)
        )
    )

fun CompoundTag.getText(name: String): Text? {
    val tag = get(name) ?: return null

    return Text.Serializer.fromJson(
        Dynamic.convert(
            NbtOps.INSTANCE,
            JsonOps.INSTANCE,
            tag
        )
    )
}

fun CompoundTag.containsOldUuid(key: String): Boolean =
    contains("${key}Most") && contains("${key}Least")

fun CompoundTag.getOldUuid(key: String): UUID =
    UUID(getLong("${key}Most"), getLong("${key}Least"))

package juuxel.adorn.util

import com.mojang.datafixers.Dynamic
import com.mojang.datafixers.types.JsonOps
import net.minecraft.datafixers.NbtOps
import net.minecraft.nbt.CompoundTag
import net.minecraft.text.Text
import java.util.Optional

fun CompoundTag.putTextComponent(name: String, textComponent: Text) =
    put(
        name,
        Dynamic.convert(
            JsonOps.INSTANCE,
            NbtOps.INSTANCE,
            Text.Serializer.toJsonTree(textComponent)
        )
    )

fun CompoundTag.getTextComponent(name: String): Text? {
    val tag = getTag(name) ?: return null

    return Text.Serializer.fromJson(
        Dynamic.convert(
            NbtOps.INSTANCE,
            JsonOps.INSTANCE,
            tag
        )
    )
}

fun <T> Optional<Optional<T>>.flatten(): Optional<T> = orElse(Optional.empty())

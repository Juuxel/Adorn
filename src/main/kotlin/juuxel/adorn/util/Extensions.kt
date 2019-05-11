package juuxel.adorn.util

import com.mojang.datafixers.Dynamic
import com.mojang.datafixers.types.JsonOps
import com.mojang.datafixers.util.Pair as DataFixPair
import net.minecraft.datafixers.NbtOps
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import java.util.Optional

fun CompoundTag.putTextComponent(name: String, textComponent: Component) =
    put(
        name,
        Dynamic.convert(
            JsonOps.INSTANCE,
            NbtOps.INSTANCE,
            Component.Serializer.toJson(textComponent)
        )
    )

fun CompoundTag.getTextComponent(name: String): Component? {
    val tag = getTag(name) ?: return null

    return Component.Serializer.fromJson(
        Dynamic.convert(
            NbtOps.INSTANCE,
            JsonOps.INSTANCE,
            tag
        )
    )
}

fun <T> Optional<Optional<T>>.flatten(): Optional<T> = orElse(Optional.empty())

operator fun <F, S> DataFixPair<F, S>.component1(): F = first
operator fun <F, S> DataFixPair<F, S>.component2(): S = second

package juuxel.adorn.util

import com.mojang.datafixers.Dynamic
import com.mojang.datafixers.types.JsonOps
import net.minecraft.datafixers.NbtOps
import net.minecraft.nbt.CompoundTag
import net.minecraft.text.TextComponent

fun CompoundTag.putTextComponent(name: String, textComponent: TextComponent) =
    put(
        name,
        Dynamic.convert(
            JsonOps.INSTANCE,
            NbtOps.INSTANCE,
            TextComponent.Serializer.toJson(textComponent)
        )
    )

fun CompoundTag.getTextComponent(name: String): TextComponent? {
    val tag = getTag(name) ?: return null

    return TextComponent.Serializer.fromJson(
        Dynamic.convert(
            NbtOps.INSTANCE,
            JsonOps.INSTANCE,
            tag
        )
    )
}

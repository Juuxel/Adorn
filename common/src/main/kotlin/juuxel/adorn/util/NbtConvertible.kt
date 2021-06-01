package juuxel.adorn.util

import net.minecraft.nbt.CompoundTag

interface NbtConvertible {
    fun fromTag(tag: CompoundTag)
    fun toTag(tag: CompoundTag): CompoundTag
}

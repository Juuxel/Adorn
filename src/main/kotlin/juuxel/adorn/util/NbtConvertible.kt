package juuxel.adorn.util

import net.minecraft.nbt.NbtCompound

interface NbtConvertible {
    fun fromTag(tag: NbtCompound)
    fun toTag(tag: NbtCompound): NbtCompound
}

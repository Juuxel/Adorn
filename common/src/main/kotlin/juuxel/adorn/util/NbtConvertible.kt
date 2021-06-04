package juuxel.adorn.util

import net.minecraft.nbt.NbtCompound

interface NbtConvertible {
    fun readNbt(nbt: NbtCompound)
    fun writeNbt(nbt: NbtCompound): NbtCompound
}

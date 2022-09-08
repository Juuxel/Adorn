package juuxel.adorn.platform.forge.networking

import net.minecraft.network.PacketByteBuf

interface Message {
    fun write(buf: PacketByteBuf)
}

package juuxel.adorn.network

import io.netty.buffer.Unpooled
import net.minecraft.util.PacketByteBuf

abstract class AdornPacket {
    abstract fun write(buf: PacketByteBuf): PacketByteBuf

    fun toBuf(): PacketByteBuf = write(PacketByteBuf(Unpooled.buffer()))
}

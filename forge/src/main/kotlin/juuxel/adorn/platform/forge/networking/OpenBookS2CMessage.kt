package juuxel.adorn.platform.forge.networking

import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier

data class OpenBookS2CMessage(val bookId: Identifier) : Message {
    override fun write(buf: PacketByteBuf) {
        buf.writeIdentifier(bookId)
    }

    companion object {
        fun fromPacket(buf: PacketByteBuf): OpenBookS2CMessage =
            OpenBookS2CMessage(buf.readIdentifier())
    }
}

package juuxel.adorn.platform.forge.networking

import net.minecraft.network.PacketByteBuf
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier

data class OpenBookS2CMessage(val bookId: Identifier) : CustomPayload {
    override fun id(): Identifier = AdornNetworking.OPEN_BOOK

    override fun write(buf: PacketByteBuf) {
        buf.writeIdentifier(bookId)
    }

    companion object {
        fun fromPacket(buf: PacketByteBuf): OpenBookS2CMessage =
            OpenBookS2CMessage(buf.readIdentifier())
    }
}

package juuxel.adorn.platform.forge.networking

import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier

data class SetTradeStackC2SMessage(val syncId: Int, val slotId: Int, val stack: ItemStack) : CustomPayload {
    override fun id(): Identifier = AdornNetworking.SET_TRADE_STACK

    override fun write(buf: PacketByteBuf) {
        buf.writeVarInt(syncId)
        buf.writeVarInt(slotId)
        buf.writeItemStack(stack)
    }

    companion object {
        fun fromPacket(buf: PacketByteBuf): SetTradeStackC2SMessage =
            SetTradeStackC2SMessage(
                buf.readVarInt(),
                buf.readVarInt(),
                buf.readItemStack()
            )
    }
}

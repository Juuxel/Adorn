package juuxel.adorn.platform.forge.networking

import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf

data class SetTradeStackC2SMessage(val syncId: Int, val slotId: Int, val stack: ItemStack) : Message {
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

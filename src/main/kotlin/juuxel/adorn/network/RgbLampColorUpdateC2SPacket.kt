package juuxel.adorn.network

import net.minecraft.util.PacketByteBuf
import net.minecraft.util.math.BlockPos

@UseExperimental(ExperimentalUnsignedTypes::class)
data class RgbLampColorUpdateC2SPacket(val syncId: Int, val pos: BlockPos, val red: UByte, val green: UByte, val blue: UByte) {
    fun write(buf: PacketByteBuf): PacketByteBuf {
        buf.writeInt(syncId)
        buf.writeBlockPos(pos)
        buf.writeByte(red.toInt())
        buf.writeByte(green.toInt())
        buf.writeByte(blue.toInt())
        return buf
    }

    companion object {
        fun read(buf: PacketByteBuf): RgbLampColorUpdateC2SPacket =
            RgbLampColorUpdateC2SPacket(
                syncId = buf.readInt(),
                pos = buf.readBlockPos(),
                red = buf.readByte().toUByte(),
                green = buf.readByte().toUByte(),
                blue = buf.readByte().toUByte()
            )
    }
}

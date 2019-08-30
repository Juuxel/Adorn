package juuxel.adorn.network

import juuxel.adorn.util.colorFromComponents
import net.minecraft.util.PacketByteBuf
import net.minecraft.util.math.BlockPos

data class RgbLampColorUpdateS2CPacket(
    val pos: BlockPos,
    val red: Int, val green: Int, val blue: Int
) : AdornPacket() {
    override fun write(buf: PacketByteBuf): PacketByteBuf {
        buf.writeBlockPos(pos)
        buf.writeInt(colorFromComponents(red, green, blue))
        return buf
    }

    companion object {
        fun read(buf: PacketByteBuf): RgbLampColorUpdateS2CPacket {
            val pos = buf.readBlockPos()
            val combinedColor = buf.readInt()
            val red = (combinedColor shr 16) and 0xFF
            val green = (combinedColor shr 8) and 0xFF
            val blue = combinedColor and 0xFF

            return RgbLampColorUpdateS2CPacket(pos, red, green, blue)
        }
    }
}

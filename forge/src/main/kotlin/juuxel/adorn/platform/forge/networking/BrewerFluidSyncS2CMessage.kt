package juuxel.adorn.platform.forge.networking

import juuxel.adorn.fluid.FluidVolume
import net.minecraft.network.PacketByteBuf

data class BrewerFluidSyncS2CMessage(val syncId: Int, val fluid: FluidVolume) {
    fun write(buf: PacketByteBuf) {
        buf.writeByte(syncId)
        fluid.write(buf)
    }

    companion object {
        fun fromPacket(buf: PacketByteBuf): BrewerFluidSyncS2CMessage =
            BrewerFluidSyncS2CMessage(
                syncId = buf.readUnsignedByte().toInt(),
                fluid = FluidVolume.load(buf)
            )
    }
}

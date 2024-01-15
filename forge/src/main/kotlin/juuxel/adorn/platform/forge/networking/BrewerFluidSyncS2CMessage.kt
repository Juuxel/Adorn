package juuxel.adorn.platform.forge.networking

import juuxel.adorn.fluid.FluidVolume
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier

data class BrewerFluidSyncS2CMessage(val syncId: Int, val fluid: FluidVolume) : CustomPayload {
    override fun id(): Identifier = AdornNetworking.BREWER_FLUID_SYNC

    override fun write(buf: PacketByteBuf) {
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

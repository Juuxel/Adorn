package juuxel.adorn.fluid

import juuxel.adorn.platform.FluidRenderingBridge
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.registry.Registry

abstract class FluidReference {
    abstract var fluid: Fluid
    abstract var amount: Long
    abstract var nbt: NbtCompound?
    val isEmpty: Boolean get() = fluid == Fluids.EMPTY || amount == 0L
    /** The amount in litres if [amount] is in platform units. */
    var amountInLitres: Long
        get() = amount * 1000 / FluidRenderingBridge.get().bucketVolume
        set(value) {
            amount = value * FluidRenderingBridge.get().bucketVolume / 1000
        }

    fun write(buf: PacketByteBuf) {
        if (isEmpty) {
            buf.writeBoolean(false)
        } else {
            buf.writeBoolean(true)
            buf.writeVarInt(Registry.FLUID.getRawId(fluid))
            buf.writeVarLong(amount)
            buf.writeNbt(nbt)
        }
    }

    fun read(buf: PacketByteBuf) {
        if (buf.readBoolean()) {
            fluid = Registry.FLUID[buf.readVarInt()]
            amount = buf.readVarLong()
            nbt = buf.readNbt()
        } else {
            fluid = Fluids.EMPTY
            amount = 0
            nbt = null
        }
    }

    fun copy(): FluidVolume = FluidVolume(fluid, amount, nbt)

    companion object {
        fun areFluidsEqual(a: FluidReference, b: FluidReference): Boolean {
            if (a.isEmpty) return b.isEmpty
            return a.fluid == b.fluid && a.nbt == b.nbt
        }

        fun areContentsEqual(a: FluidReference, b: FluidReference): Boolean {
            if (a.isEmpty) return b.isEmpty
            return areFluidsEqual(a, b) && a.amount == b.amount
        }

        // Potentially lossy.
        fun convertToPlatform(amount: Long): Long =
            amount * FluidRenderingBridge.get().bucketVolume / 1000

        // Potentially lossy.
        fun convertToLitres(amount: Long): Long =
            amount * 1000 / FluidRenderingBridge.get().bucketVolume
    }
}

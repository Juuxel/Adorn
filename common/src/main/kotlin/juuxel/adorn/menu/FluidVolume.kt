package juuxel.adorn.menu

import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.registry.Registry

abstract class FluidVolume {
    abstract var fluid: Fluid
    abstract var amount: Long
    abstract var nbt: NbtCompound?
    val isEmpty: Boolean get() = fluid == Fluids.EMPTY || amount == 0L

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

    fun copy(): FluidVolume = Simple(fluid, amount, nbt)

    companion object {
        fun areEqual(a: FluidVolume, b: FluidVolume): Boolean {
            if (a.isEmpty) return b.isEmpty

            return a.fluid == b.fluid && a.amount == b.amount && a.nbt == b.nbt
        }

        fun empty(): FluidVolume =
            Simple(Fluids.EMPTY, 0L, null)

        fun load(buf: PacketByteBuf): FluidVolume =
            empty().apply { read(buf) }
    }

    private data class Simple(override var fluid: Fluid, override var amount: Long, override var nbt: NbtCompound?) : FluidVolume()
}

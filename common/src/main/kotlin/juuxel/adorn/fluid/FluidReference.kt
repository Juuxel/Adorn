package juuxel.adorn.fluid

import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.registry.Registry

/**
 * A mutable reference to a fluid volume.
 * This can be a [FluidVolume] or a block entity's
 * internal fluid volume.
 */
abstract class FluidReference : HasFluidAmount {
    abstract var fluid: Fluid
    abstract override var amount: Long
    abstract var nbt: NbtCompound?
    val isEmpty: Boolean get() = fluid == Fluids.EMPTY || amount == 0L

    fun write(buf: PacketByteBuf) {
        buf.writeEnumConstant(unit)

        if (isEmpty) {
            buf.writeBoolean(false)
        } else {
            buf.writeBoolean(true)
            buf.writeVarInt(Registry.FLUID.getRawId(fluid))
            buf.writeVarLong(amount)
            buf.writeNbt(nbt)
        }
    }

    protected fun readWithoutUnit(buf: PacketByteBuf) {
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

    fun copy(): FluidVolume = FluidVolume(fluid, amount, nbt, unit)

    fun increment(amount: Long, unit: FluidUnit) {
        this.amount += FluidUnit.convert(amount, this.unit, unit)
    }

    fun decrement(amount: Long, unit: FluidUnit) =
        increment(-amount, unit)

    fun matches(ingredient: FluidIngredient): Boolean =
        ingredient.fluid.matches(fluid) && FluidUnit.compareVolumes(this, ingredient) >= 0 && nbt == ingredient.nbt

    override fun toString() =
        "FluidReference(fluid=${Registry.FLUID.getId(fluid)}, amount=$amount, nbt=$nbt)"

    companion object {
        fun areFluidsEqual(a: FluidReference, b: FluidReference): Boolean {
            if (a.isEmpty) return b.isEmpty
            return a.fluid == b.fluid && a.nbt == b.nbt
        }

        fun areFluidsAndAmountsEqual(a: FluidReference, b: FluidReference): Boolean {
            if (a.isEmpty) return b.isEmpty
            return areFluidsEqual(a, b) && FluidUnit.compareVolumes(a, b) == 0
        }
    }
}

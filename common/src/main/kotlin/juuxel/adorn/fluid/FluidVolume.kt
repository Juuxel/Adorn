package juuxel.adorn.fluid

import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.PacketByteBuf
import net.minecraft.registry.Registries

data class FluidVolume(
    override var fluid: Fluid,
    override var amount: Long,
    override var nbt: NbtCompound?,
    override val unit: FluidUnit
) : FluidReference() {
    override fun toString() =
        "FluidVolume(fluid=${Registries.FLUID.getId(fluid)}, amount=$amount, nbt=$nbt)"

    companion object {
        fun empty(unit: FluidUnit): FluidVolume =
            FluidVolume(Fluids.EMPTY, 0L, null, unit)

        fun load(buf: PacketByteBuf): FluidVolume =
            empty(buf.readEnumConstant(FluidUnit::class.java)).apply { readWithoutUnit(buf) }
    }
}

package juuxel.adorn.fluid

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.registry.Registry

data class FluidVolume(override var fluid: Fluid, override var amount: Long, override var nbt: NbtCompound?) : FluidReference() {
    override fun toString() =
        "FluidVolume(fluid=${Registry.FLUID.getId(fluid)}, amount=$amount, nbt=$nbt)"

    companion object {
        val NO_NBT_CODEC: Codec<FluidVolume> = RecordCodecBuilder.create { instance ->
            instance.group(
                Registry.FLUID.codec.fieldOf("fluid").forGetter { it.fluid },
                Codec.LONG.fieldOf("amount").forGetter { it.amount },
            ).apply(instance) { fluid, amount -> FluidVolume(fluid, amount, null) }
        }

        fun empty(): FluidVolume =
            FluidVolume(Fluids.EMPTY, 0L, null)

        fun load(buf: PacketByteBuf): FluidVolume =
            empty().apply { read(buf) }
    }
}

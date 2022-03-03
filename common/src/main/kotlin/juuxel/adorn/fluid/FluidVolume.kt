package juuxel.adorn.fluid

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.registry.Registry
import java.util.Optional

data class FluidVolume(
    override var fluid: Fluid,
    override var amount: Long,
    override var nbt: NbtCompound?,
    override val unit: FluidUnit
) : FluidReference() {
    // for DFU
    private constructor(fluid: Fluid, amount: Long, nbt: Optional<NbtCompound>, unit: FluidUnit) :
        this(fluid, amount, nbt.orElse(null), unit)

    override fun toString() =
        "FluidVolume(fluid=${Registry.FLUID.getId(fluid)}, amount=$amount, nbt=$nbt)"

    companion object {
        val CODEC: Codec<FluidVolume> = RecordCodecBuilder.create { instance ->
            instance.group(
                Registry.FLUID.codec.fieldOf("fluid").forGetter { it.fluid },
                Codec.LONG.fieldOf("amount").forGetter { it.amount },
                NbtCompound.CODEC.optionalFieldOf("nbt").forGetter { Optional.ofNullable(it.nbt) },
                FluidUnit.CODEC.optionalFieldOf("unit", FluidUnit.LITRE).forGetter { it.unit },
            ).apply(instance, ::FluidVolume)
        }

        fun empty(unit: FluidUnit): FluidVolume =
            FluidVolume(Fluids.EMPTY, 0L, null, unit)

        fun load(buf: PacketByteBuf): FluidVolume =
            empty(buf.readEnumConstant(FluidUnit::class.java)).apply { readWithoutUnit(buf) }
    }
}

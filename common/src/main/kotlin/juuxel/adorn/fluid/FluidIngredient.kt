package juuxel.adorn.fluid

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.PacketByteBuf
import java.util.Optional

/**
 * A fluid ingredient for crafting.
 */
data class FluidIngredient(
    val fluid: FluidKey,
    override val amount: Long,
    val nbt: NbtCompound?,
    override val unit: FluidUnit
) : HasFluidAmount {
    // for DFU
    private constructor(fluid: FluidKey, amount: Long, nbt: Optional<NbtCompound>, unit: FluidUnit) :
        this(fluid, amount, nbt.orElse(null), unit)

    fun write(buf: PacketByteBuf) {
        fluid.write(buf)
        buf.writeVarLong(amount)
        buf.writeNbt(nbt)
        buf.writeEnumConstant(unit)
    }

    companion object {
        val CODEC: Codec<FluidIngredient> = RecordCodecBuilder.create { instance ->
            instance.group(
                FluidKey.CODEC.fieldOf("fluid").forGetter { it.fluid },
                Codec.LONG.fieldOf("amount").forGetter { it.amount },
                NbtCompound.CODEC.optionalFieldOf("nbt").forGetter { Optional.ofNullable(it.nbt) },
                FluidUnit.CODEC.optionalFieldOf("unit", FluidUnit.LITRE).forGetter { it.unit },
            ).apply(instance, ::FluidIngredient)
        }

        fun load(buf: PacketByteBuf): FluidIngredient {
            val fluid = FluidKey.load(buf)
            val amount = buf.readVarLong()
            val nbt = buf.readNbt()
            val unit = buf.readEnumConstant(FluidUnit::class.java)
            return FluidIngredient(fluid, amount, nbt, unit)
        }
    }
}

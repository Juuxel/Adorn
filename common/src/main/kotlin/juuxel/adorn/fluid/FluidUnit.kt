package juuxel.adorn.fluid

import com.mojang.serialization.Codec
import juuxel.adorn.util.Displayable
import juuxel.adorn.util.MixedFraction
import net.minecraft.text.Text

/**
 * Fluid volume units. This class is used for doing fluid volume
 * math in the common module (fluid-based recipes).
 *
 * The platform-specific unit is available via
 * [FluidBridge.fluidUnit][juuxel.adorn.platform.FluidBridge.fluidUnit].
 */
enum class FluidUnit(val id: String, val bucketVolume: Long) : Displayable {
    /** Litres. Defined as one thousandth of a cubic metre ([bucketVolume] = 1000). */
    LITRE("litres", 1000),

    /** Droplets. Defined as 1/81 000 of a cubic metre ([bucketVolume] = 81 000). */
    DROPLET("droplets", 81_000);

    override val displayName: Text = Text.translatable("gui.adorn.fluid_unit.$id.name")
    val symbol: Text = Text.translatable("gui.adorn.fluid_unit.$id.symbol")

    companion object {
        private val BY_ID: Map<String, FluidUnit> = values().associateBy { it.id }
        val CODEC: Codec<FluidUnit> = Codec.STRING.xmap(this::byId, FluidUnit::id)

        /**
         * Returns the fluid unit with the specified [ID][FluidUnit.id],
         * or null if not found.
         */
        fun byId(id: String): FluidUnit? = BY_ID[id.lowercase()]

        /**
         * Converts a volume between two fluid units. Potentially lossy, use with caution!
         */
        fun convert(volume: Long, from: FluidUnit, to: FluidUnit): Long {
            if (from == to) return volume
            return volume * to.bucketVolume / from.bucketVolume
        }

        /**
         * Converts a volume between two fluid units losslessly, returning a mixed fraction.
         */
        fun losslessConvert(volume: Long, from: FluidUnit, to: FluidUnit): MixedFraction {
            if (from == to) return MixedFraction.whole(volume)
            return MixedFraction(volume * to.bucketVolume, from.bucketVolume)
        }

        /**
         * Converts a volume between two fluid units.
         * This variant is meant to be used for rendering.
         */
        fun convertAsDouble(volume: Double, from: FluidUnit, to: FluidUnit): Double {
            if (from == to) return volume
            return volume * to.bucketVolume.toDouble() / from.bucketVolume.toDouble()
        }

        /**
         * Compares two fluid volumes with specified units.
         */
        fun compareVolumes(volume1: Long, unit1: FluidUnit, volume2: Long, unit2: FluidUnit): Int =
            if (unit1 == unit2) {
                volume1.compareTo(volume2)
            } else if (unit1.bucketVolume > unit2.bucketVolume) {
                volume1.compareTo(volume2 * unit1.bucketVolume / unit2.bucketVolume)
            } else {
                (volume1 * unit2.bucketVolume / unit1.bucketVolume).compareTo(volume2)
            }

        /**
         * Compares the amounts of two [FluidReference]s.
         */
        fun compareVolumes(volume1: HasFluidAmount, volume2: HasFluidAmount): Int =
            compareVolumes(volume1.amount, volume1.unit, volume2.amount, volume2.unit)
    }
}

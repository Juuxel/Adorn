package juuxel.adorn.fluid

import net.minecraft.fluid.Fluids

interface FluidAmountPredicate {
    val upperBound: HasFluidAmount

    fun test(amount: Long, unit: FluidUnit): Boolean

    companion object {
        fun exactly(amount: Long, unit: FluidUnit): FluidAmountPredicate {
            return object : FluidAmountPredicate {
                override val upperBound = FluidVolume(Fluids.EMPTY, amount, null, unit)

                override fun test(amount: Long, unit: FluidUnit): Boolean =
                    FluidUnit.compareVolumes(amount, unit, upperBound.amount, upperBound.unit) == 0
            }
        }

        fun atMost(max: Long, unit: FluidUnit): FluidAmountPredicate {
            return object : FluidAmountPredicate {
                override val upperBound = FluidVolume(Fluids.EMPTY, max, null, unit)

                override fun test(amount: Long, unit: FluidUnit): Boolean =
                    FluidUnit.compareVolumes(amount, unit, upperBound.amount, upperBound.unit) == 0
            }
        }
    }
}

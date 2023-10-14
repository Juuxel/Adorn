package juuxel.adorn.fluid

class StepMaximum(
    val min: Long,
    val max: Long,
    val step: Long,
    val unit: FluidUnit
) : FluidAmountPredicate {
    override val upperBound = object : HasFluidAmount {
        override val amount = max
        override val unit = this@StepMaximum.unit
    }

    init {
        require(min < max) { "min must be less than max" }
        require((max - min) % step == 0L) { "max - min must be divisible by step" }
    }

    override fun test(amount: Long, unit: FluidUnit): Boolean {
        val toCompare = FluidUnit.convert(amount, from = unit, to = this.unit)

        if (toCompare < min || toCompare > max) {
            return false
        }

        val zeroed = toCompare - min
        return zeroed % step == 0L
    }
}

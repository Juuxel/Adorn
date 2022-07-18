package juuxel.adorn.util

class MixedFraction private constructor(val whole: Long, val numerator: Long, val denominator: Long) {
    init {
        require(numerator >= 0L) {
            "Numerator must not be negative, was $numerator"
        }

        require(denominator > 0L) {
            "Denominator must not be 0 or negative, was $denominator"
        }

        require(denominator != 1L || numerator == 0L) {
            "Denominator 1 is only allowed when numerator == 0, was $numerator"
        }
    }

    fun resizeFraction(newDenominator: Long): MixedFraction =
        if (numerator == 0L) this
        else MixedFraction(whole, numerator * newDenominator / denominator, newDenominator)

    override fun toString() = Fractions.toString(whole, numerator, denominator)

    companion object {
        fun whole(n: Long): MixedFraction = MixedFraction(n, 0, 1)

        operator fun invoke(numerator: Long, denominator: Long): MixedFraction {
            if (denominator == 1L) return MixedFraction(numerator, 0, 1)
            val realNumerator = numerator % denominator
            val whole = (numerator - realNumerator) / denominator
            return MixedFraction(whole, realNumerator, denominator)
        }
    }
}

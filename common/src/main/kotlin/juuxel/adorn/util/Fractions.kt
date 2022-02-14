package juuxel.adorn.util

import kotlin.math.abs
import kotlin.math.sign

object Fractions {
    private val SUPERSCRIPTS = charArrayOf('\u2070', '\u00B9', '\u00B2', '\u00B3', '\u2074', '\u2075', '\u2076', '\u2077', '\u2078', '\u2079')
    private val SUBSCRIPTS = charArrayOf('\u2080', '\u2081', '\u2082', '\u2083', '\u2084', '\u2085', '\u2086', '\u2087', '\u2088', '\u2089')
    private const val FRACTION_BAR = '\u2044'

    fun toString(numerator: Long, denominator: Long): String {
        val realNumerator = numerator % denominator
        val whole = (numerator - realNumerator) / denominator
        return toString(whole, realNumerator, denominator)
    }

    fun toString(whole: Long, numerator: Long, denominator: Long): String = buildString {
        if (whole.sign * numerator.sign * denominator.sign == -1) {
            append('-')
        }

        append(abs(whole))

        if (numerator != 0L) {
            append(' ')

            for (digit in digits(numerator)) {
                append(SUPERSCRIPTS[digit])
            }
            append(FRACTION_BAR)
            for (digit in digits(denominator)) {
                append(SUBSCRIPTS[digit])
            }
        }
    }

    private fun digits(long: Long): IntArray {
        val abs = abs(long)
        val str = abs.toString()
        val result = IntArray(str.length)

        for ((i, char) in str.withIndex()) {
            result[i] = when (char) {
                // TODO: replace with subtraction
                '0' -> 0
                '1' -> 1
                '2' -> 2
                '3' -> 3
                '4' -> 4
                '5' -> 5
                '6' -> 6
                '7' -> 7
                '8' -> 8
                '9' -> 9
                else -> error("Unknown digit character: $char")
            }
        }

        return result
    }
}

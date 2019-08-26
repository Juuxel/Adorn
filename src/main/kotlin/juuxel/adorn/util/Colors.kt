package juuxel.adorn.util

fun color(rgb: Int, alpha: Int = 0xFF) =
    alpha shl 24 or rgb

@UseExperimental(ExperimentalUnsignedTypes::class)
fun colorFromUBytes(red: UByte, green: UByte, blue: UByte): Int =
    (red.toInt() shl 16) or (green.toInt() shl 8) or (blue.toInt())

object Colors {
    val BLACK = color(0x000000)
    val WHITE = color(0xFFFFFF)
}

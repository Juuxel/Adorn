package juuxel.adorn.util

fun color(rgb: Int, alpha: Int = 0xFF) =
    alpha shl 24 or rgb

object Colors {
    val BLACK = color(0x000000)
    val WHITE = color(0xFFFFFF)
}

package juuxel.adorn.util

fun color(rgb: Int, alpha: Int = 0xFF) =
    alpha shl 24 or rgb

fun color(rgb: Int, alpha: Float) = color(rgb, (alpha * 255f).toInt())

// fun colorFromComponents(red: Int, green: Int, blue: Int): Int =
//    ((red and 0xFF) shl 16) or ((green and 0xFF) shl 8) or (blue and 0xFF)

object Colors {
    val BLACK = color(0x000000)
    val WHITE = color(0xFFFFFF)
    val SCREEN_TEXT = color(0x404040)
}

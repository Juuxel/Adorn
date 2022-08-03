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
    val TRANSPARENT = color(0x000000, alpha = 0)

    fun redOf(argb: Int): Float = ((argb shr 16) and 0xFF) / 255f
    fun greenOf(argb: Int): Float = ((argb shr 8) and 0xFF) / 255f
    fun blueOf(argb: Int): Float = (argb and 0xFF) / 255f
    fun alphaOf(argb: Int): Float = ((argb shr 24) and 0xFF) / 255f
}

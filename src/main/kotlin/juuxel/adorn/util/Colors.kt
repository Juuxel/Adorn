package juuxel.adorn.util

fun color(rgb: Int, alpha: Int = 0xFF) =
    alpha shl 24 or rgb

package juuxel.adorn.util

fun color(color: Int, alpha: Int = 0xFF) =
    alpha shl 24 or color

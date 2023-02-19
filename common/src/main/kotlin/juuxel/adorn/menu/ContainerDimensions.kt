package juuxel.adorn.menu

data class ContainerDimensions(val width: Int, val height: Int) {
    val size: Int = width * height
}

@Suppress("NOTHING_TO_INLINE")
inline infix fun Int.by(height: Int) = ContainerDimensions(this, height)

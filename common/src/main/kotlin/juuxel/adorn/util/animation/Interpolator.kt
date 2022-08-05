package juuxel.adorn.util.animation

import juuxel.adorn.util.Colors
import juuxel.adorn.util.color
import net.minecraft.util.math.MathHelper

fun interface Interpolator<T> {
    fun interpolate(delta: Float, from: T, to: T): T

    companion object {
        val FLOAT: Interpolator<Float> = Interpolator(MathHelper::lerp)
        val DOUBLE: Interpolator<Double> = Interpolator { delta, from, to -> MathHelper.lerp(delta.toDouble(), from, to) }
        val COLOR: Interpolator<Int> = Interpolator { delta, from, to ->
            val alpha = FLOAT.interpolate(delta, Colors.alphaOf(from), Colors.alphaOf(to))
            val red = FLOAT.interpolate(delta, Colors.redOf(from), Colors.redOf(to))
            val green = FLOAT.interpolate(delta, Colors.greenOf(from), Colors.greenOf(to))
            val blue = FLOAT.interpolate(delta, Colors.blueOf(from), Colors.blueOf(to))
            color(red, green, blue, alpha)
        }
    }
}

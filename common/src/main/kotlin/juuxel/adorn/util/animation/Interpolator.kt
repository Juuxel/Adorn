package juuxel.adorn.util.animation

import net.minecraft.util.math.MathHelper

fun interface Interpolator<T> {
    fun interpolate(delta: Float, from: T, to: T): T

    companion object {
        val FLOAT: Interpolator<Float> = Interpolator(MathHelper::lerp)
        val DOUBLE: Interpolator<Double> = Interpolator { delta, from, to -> MathHelper.lerp(delta.toDouble(), from, to) }
    }
}

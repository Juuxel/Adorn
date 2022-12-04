package juuxel.adorn.util.animation

import kotlin.reflect.KProperty

class AnimatedProperty<T>(
    initial: T,
    engine: AnimationEngine,
    duration: Int,
    interpolator: Interpolator<T>
) : AbstractAnimatedProperty<T>(engine, duration, interpolator) {
    private var value: T = initial

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = value
    override fun setRawValue(value: T) {
        this.value = value
    }
}

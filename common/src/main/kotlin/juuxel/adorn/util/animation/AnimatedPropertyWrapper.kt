package juuxel.adorn.util.animation

import kotlin.reflect.KProperty

class AnimatedPropertyWrapper<T>(
    engine: AnimationEngine,
    duration: Int,
    interpolator: Interpolator<T>,
    private val getter: () -> T,
    private val setter: (T) -> Unit
) : AbstractAnimatedProperty<T>(engine, duration, interpolator) {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T = getter()
    override fun setRawValue(value: T) = setter(value)
}

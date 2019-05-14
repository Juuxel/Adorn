package juuxel.adorn.block.property

import net.minecraft.state.property.AbstractProperty
import net.minecraft.state.property.Property
import net.minecraft.util.StringIdentifiable
import java.util.Optional

@Suppress("UNCHECKED_CAST")
class OptionalProperty<T>(
    private val delegate: Property<T>
) : AbstractProperty<OptionalProperty.Value<T>>(delegate.name, Value::class.java as Class<Value<T>>)
    where T : Comparable<T>, T : StringIdentifiable {
    val none: Value.None<T> = Value.None<T>()
    private val values = delegate.values.map { Value.Some(it) } + none

    init {
        require(delegate.values.none { it.asString() == "none" }) {
            "delegate has a 'none' value"
        }
    }

    override fun getValue(str: String?): Optional<Value<T>> = when (str) {
        "none" -> Optional.of(none)
        else -> delegate.getValue(str).map { Value.Some(it) }
    }

    override fun getValues() = values

    override fun getValueAsString(value: Value<T>) = when (value) {
        is Value.Some -> value.value.asString()
        is Value.None -> "none"
    }

    fun wrap(value: T) = Value.Some(value)

    sealed class Value<T : Comparable<T>> : Comparable<Value<T>> {
        abstract val isPresent: Boolean
        abstract val value: T?

        data class Some<T : Comparable<T>>(override val value: T) : Value<T>() {
            override val isPresent = true

            override fun compareTo(other: Value<T>) = when (other) {
                is Some -> value.compareTo(other.value)
                is None -> 1
            }
        }

        class None<T : Comparable<T>> : Value<T>() {
            override val isPresent = false
            override val value: T? = null

            override fun compareTo(other: Value<T>) = when (other) {
                is Some -> -1
                is None -> 0
            }
        }
    }
}

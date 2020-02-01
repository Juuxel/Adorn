package juuxel.adorn.block.property

import java.util.Optional
import net.minecraft.state.property.AbstractProperty
import net.minecraft.state.property.Property
import net.minecraft.util.StringIdentifiable

@Suppress("UNCHECKED_CAST")
class OptionalProperty<T>(
    private val delegate: Property<T>
) : AbstractProperty<OptionalProperty.Value<T>>(delegate.name, Value::class.java as Class<Value<T>>)
    where T : Any, T : Comparable<T>, T : StringIdentifiable {
    val none: Value.None<T> = Value.None()
    private val values: Map<T?, Value<T>> = sequence {
        for (value in delegate.values) {
            yield(value to Value.Some<T>(value))
        }

        yield(null to none)
    }.toMap()

    init {
        require(delegate.values.none { it.asString() == "none" }) {
            "delegate has a 'none' value"
        }
    }

    override fun parse(str: String?): Optional<Value<T>> = when (str) {
        "none" -> Optional.of(none)
        else -> delegate.parse(str).map { values[it] }
    }

    override fun getValues() = values.values

    override fun name(value: Value<T>) = value.value?.asString() ?: "none"

    fun wrap(value: T): Value<T>? = values[value]
    fun wrapOrNone(value: T): Value<T> = values[value] ?: none

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

package juuxel.adorn.util

abstract class Event<L, in I> {
    private val listeners: MutableList<L> = ArrayList()

    fun addListener(listener: L) {
        listeners += listener
    }

    fun removeListener(listener: L) {
        listeners -= listener
    }

    fun execute(input: I) {
        for (listener in listeners) {
            execute(listener, input)
        }
    }

    protected abstract fun execute(listener: L, input: I)

    companion object {
        operator fun <L, I> invoke(executor: (L, I) -> Unit): Event<L, I> =
            object : Event<L, I>() {
                override fun execute(listener: L, input: I) =
                    executor(listener, input)
            }
    }
}

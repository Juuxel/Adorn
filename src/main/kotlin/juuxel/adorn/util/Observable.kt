package juuxel.adorn.util

typealias Listener<T> = (T) -> Unit

open class Observable<This : Observable<This>> {
    private val listeners: MutableList<Listener<This>> = ArrayList()

    fun addListener(listener: Listener<This>) {
        listeners += listener
    }

    @Suppress("UNCHECKED_CAST")
    fun callListeners(): Unit = listeners.forEach {
        it(this as This)
    }
}

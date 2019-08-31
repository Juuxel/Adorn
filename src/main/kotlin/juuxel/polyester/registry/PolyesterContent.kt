package juuxel.polyester.registry

@Deprecated("PolyesterContent can be replaced with a cleaner registry system.")
interface PolyesterContent<out T> {
    val name: String

    @Suppress("UNCHECKED_CAST")
    fun unwrap(): T = this as T
}

interface HasDescription {
    val hasDescription: Boolean get() = false
    val descriptionKey: String get() = "%TranslationKey.desc"
}

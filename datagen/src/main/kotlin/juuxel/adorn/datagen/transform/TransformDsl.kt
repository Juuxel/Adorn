package juuxel.adorn.datagen.transform

/**
 * A DSL for transforming JSON files.
 */
interface TransformDsl {
    /**
     * Only applies to JSON files with a property "[key]": "[value]".
     */
    fun withProperty(key: String, value: String)

    /**
     * Adds a new property "[key]": [value].
     */
    fun add(key: String, value: Any?)

    /**
     * Removes a property by its [key].
     */
    fun remove(key: String)
}

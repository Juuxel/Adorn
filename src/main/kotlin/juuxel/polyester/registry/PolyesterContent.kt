package juuxel.polyester.registry

import juuxel.polyester.block.PolyesterBlock
import juuxel.polyester.item.PolyesterItem

interface PolyesterContent<out T> {
    val name: String

    @Suppress("UNCHECKED_CAST")
    fun unwrap(): T = this as T
}

interface HasDescription {
    val hasDescription: Boolean get() = false
    val descriptionKey: String get() = "%TranslationKey.desc"
}

@Deprecated("PolyesterBlock has been moved.", ReplaceWith("PolyesterBlock", imports = ["io.github.juuxel.polyester.block.PolyesterBlock"]))
typealias PolyesterBlock = PolyesterBlock

@Deprecated("PolyesterItem has been moved.", ReplaceWith("PolyesterItem", imports = ["io.github.juuxel.polyester.item.PolyesterItem"]))
typealias PolyesterItem = PolyesterItem

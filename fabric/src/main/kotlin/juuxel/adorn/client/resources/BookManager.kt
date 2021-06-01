package juuxel.adorn.client.resources

import blue.endless.jankson.JsonObject
import juuxel.adorn.AdornCommon
import juuxel.adorn.client.book.Book
import juuxel.adorn.resources.Json5DataLoader
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import net.minecraft.util.profiler.Profiler

@Environment(EnvType.CLIENT)
object BookManager : Json5DataLoader(AdornCommon.id("book_manager"), "adorn/books", Book.jankson().build()), IdentifiableResourceReloadListener {
    private const val PREFIX_LENGTH = "adorn_books".length
    private const val SUFFIX_LENGTH = ".json5".length
    private var books: Map<Identifier, Book> = emptyMap()

    override fun apply(jsons: Map<Identifier, JsonObject>, manager: ResourceManager, profiler: Profiler) {
        books = jsons.asSequence()
            .map { (key, value) ->
                Identifier(key.namespace, key.path.substring(PREFIX_LENGTH + 1, key.path.length - SUFFIX_LENGTH)) to value
            }.map { (key, value) ->
                key to jankson.fromJson(value, Book::class.java)
            }.toMap()
    }

    operator fun contains(id: Identifier): Boolean = books.contains(id)

    operator fun get(id: Identifier): Book =
        books.getOrElse(id) { throw IllegalArgumentException("Tried to get unknown book '$id' from BookManager") }
}

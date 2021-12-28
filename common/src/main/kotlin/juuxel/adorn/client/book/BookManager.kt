package juuxel.adorn.client.book

import com.google.gson.JsonElement
import net.minecraft.resource.JsonDataLoader
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import net.minecraft.util.profiler.Profiler

open class BookManager : JsonDataLoader(Book.GSON, DATA_TYPE) {
    private var books: Map<Identifier, Book> = emptyMap()

    override fun apply(jsons: Map<Identifier, JsonElement>, manager: ResourceManager, profiler: Profiler) {
        books = jsons.asSequence()
            .map { (key, value) ->
                val id = Identifier(key.namespace, key.path.removePrefix(DATA_TYPE))
                val book = Book.fromJson(value)
                id to book
            }
            .toMap()
    }

    operator fun contains(id: Identifier): Boolean = books.contains(id)

    operator fun get(id: Identifier): Book =
        books.getOrElse(id) { throw IllegalArgumentException("Tried to get unknown book '$id' from BookManager") }

    companion object {
        private const val DATA_TYPE = "adorn/books"
    }
}

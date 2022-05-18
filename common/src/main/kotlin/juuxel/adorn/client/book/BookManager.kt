package juuxel.adorn.client.book

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.mojang.serialization.JsonOps
import juuxel.adorn.util.logger
import net.minecraft.resource.JsonDataLoader
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import net.minecraft.util.profiler.Profiler

open class BookManager : JsonDataLoader(GSON, DATA_TYPE) {
    private var books: Map<Identifier, Book> = emptyMap()

    override fun apply(jsons: Map<Identifier, JsonElement>, manager: ResourceManager, profiler: Profiler) {
        books = jsons.asSequence()
            .mapNotNull { (key, value) ->
                val id = Identifier(key.namespace, key.path.removePrefix(DATA_TYPE))
                val book = Book.CODEC.decode(JsonOps.INSTANCE, value).get()
                book.map(
                    { id to it.first },
                    {
                        LOGGER.error("Could not load book {}: {}", id, it.message())
                        null
                    }
                )
            }
            .toMap()
    }

    operator fun contains(id: Identifier): Boolean = books.contains(id)

    operator fun get(id: Identifier): Book =
        books.getOrElse(id) { throw IllegalArgumentException("Tried to get unknown book '$id' from BookManager") }

    companion object {
        private val LOGGER = logger()
        private const val DATA_TYPE = "adorn/books"
        private val GSON = Gson()
    }
}

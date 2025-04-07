package juuxel.adorn.client.book;

import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.Map;

public class BookManager extends JsonDataLoader<Book> {
    public static final String DATA_TYPE = "adorn/books";

    private Map<Identifier, Book> books = Map.of();

    public BookManager() {
        super(Book.CODEC, ResourceFinder.json(DATA_TYPE));
    }

    @Override
    protected void apply(Map<Identifier, Book> prepared, ResourceManager manager, Profiler profiler) {
        books = Map.copyOf(prepared);
    }

    public boolean contains(Identifier id) {
        return books.containsKey(id);
    }

    public Book get(Identifier id) {
        var book = books.get(id);
        if (book == null) {
            throw new IllegalArgumentException("Tried to get unknown book '%s' from BookManager".formatted(id));
        }
        return book;
    }
}

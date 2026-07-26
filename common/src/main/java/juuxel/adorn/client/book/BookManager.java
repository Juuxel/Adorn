package juuxel.adorn.client.book;

import juuxel.adorn.AdornCommon;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.Identifier;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class BookManager extends SimpleJsonResourceReloadListener<Book> {
    public static final Identifier ID = AdornCommon.id("book_manager");
    public static final String DATA_TYPE = "adorn/books";

    private Map<Identifier, Book> books = Map.of();

    public BookManager() {
        super(Book.CODEC, FileToIdConverter.json(DATA_TYPE));
    }

    @Override
    protected void apply(Map<Identifier, Book> prepared, ResourceManager manager, ProfilerFiller profiler) {
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

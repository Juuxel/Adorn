package juuxel.adorn.client.book;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.item.AdornBookItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;
import java.util.function.Consumer;

public final class BookManager extends SimpleJsonResourceReloadListener<Book> implements AdornBookItem.BookTooltipProvider {
    public static final Identifier ID = AdornCommon.id("book_manager");
    public static final BookManager INSTANCE = new BookManager();
    public static final String DATA_TYPE = "adorn/books";

    private Map<Identifier, Book> books = Map.of();

    private BookManager() {
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

    @Override
    public void appendTooltip(Identifier bookId, Consumer<Component> builder) {
        if (contains(bookId)) {
           builder.accept(Component.translatable("book.byAuthor", get(bookId).author()).withStyle(ChatFormatting.GRAY));
        }
    }

    public static void setupTooltipProvider() {
        AdornBookItem.tooltipProvider = INSTANCE;
    }
}

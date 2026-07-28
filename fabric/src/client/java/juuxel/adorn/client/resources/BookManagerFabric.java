package juuxel.adorn.client.resources;

import juuxel.adorn.client.book.BookManager;

public final class BookManagerFabric extends BookManager {
    public static final BookManagerFabric INSTANCE = new BookManagerFabric();
}

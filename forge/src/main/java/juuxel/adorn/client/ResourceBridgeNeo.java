package juuxel.adorn.client;

import juuxel.adorn.client.book.BookManager;
import juuxel.adorn.client.resources.ColorManager;
import juuxel.adorn.client.resources.ResourceBridge;

public final class ResourceBridgeNeo implements ResourceBridge {
    private final BookManager bookManager = new BookManager();
    private final ColorManager colorManager = new ColorManager();

    @Override
    public BookManager getBookManager() {
        return bookManager;
    }

    @Override
    public ColorManager getColorManager() {
        return colorManager;
    }
}

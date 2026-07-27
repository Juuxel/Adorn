package juuxel.adorn.platform;

import juuxel.adorn.client.book.BookManager;
import juuxel.adorn.client.resources.ColorManager;

public final class ResourceBridgeNeo implements ResourceBridge {
    public static final ResourceBridgeNeo INSTANCE = new ResourceBridgeNeo();
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

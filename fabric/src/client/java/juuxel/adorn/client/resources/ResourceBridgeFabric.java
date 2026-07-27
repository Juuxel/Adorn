package juuxel.adorn.client.resources;

import juuxel.adorn.client.book.BookManager;

public final class ResourceBridgeFabric implements ResourceBridge {
    @Override
    public BookManager getBookManager() {
        return BookManagerFabric.INSTANCE;
    }

    @Override
    public ColorManager getColorManager() {
        return ColorManagerFabric.INSTANCE;
    }
}

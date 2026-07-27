package juuxel.adorn.client.resources;

import juuxel.adorn.client.book.BookManager;
import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;

public interface ResourceBridge {
    BookManager getBookManager();
    ColorManager getColorManager();

    @InlineServices.Getter
    static ResourceBridge get() {
        return Services.load(ResourceBridge.class);
    }
}

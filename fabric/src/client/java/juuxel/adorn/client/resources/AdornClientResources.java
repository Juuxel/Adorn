package juuxel.adorn.client.resources;

import juuxel.adorn.client.book.BookManager;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.server.packs.PackType;

public final class AdornClientResources {
    public static void init() {
        var resourceLoader = ResourceLoader.get(PackType.CLIENT_RESOURCES);
        resourceLoader.registerReloadListener(ColorManager.ID, ColorManager.INSTANCE);
        resourceLoader.registerReloadListener(BookManager.ID, BookManager.INSTANCE);
    }
}

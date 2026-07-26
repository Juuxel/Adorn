package juuxel.adorn.client.resources;

import juuxel.adorn.client.book.BookManager;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.Identifier;

public final class BookManagerFabric extends BookManager implements IdentifiableResourceReloadListener {
    public static final BookManagerFabric INSTANCE = new BookManagerFabric();

    @Override
    public Identifier getFabricId() {
        return ID;
    }
}

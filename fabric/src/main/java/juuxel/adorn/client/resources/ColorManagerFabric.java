package juuxel.adorn.client.resources;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.util.Identifier;

public final class ColorManagerFabric extends ColorManager implements IdentifiableResourceReloadListener {
    public static final ColorManagerFabric INSTANCE = new ColorManagerFabric();

    @Override
    public Identifier getFabricId() {
        return ID;
    }
}

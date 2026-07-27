package juuxel.adorn.client.resources;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;

public final class AdornClientResources {
    public static void init() {
        // TODO: remove use of deprecated api
        var resourceManagerHelper = ResourceManagerHelper.get(PackType.CLIENT_RESOURCES);
        resourceManagerHelper.registerReloadListener(ColorManagerFabric.INSTANCE);
        resourceManagerHelper.registerReloadListener(BookManagerFabric.INSTANCE);
    }
}

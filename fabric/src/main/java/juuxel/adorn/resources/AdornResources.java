package juuxel.adorn.resources;

import juuxel.adorn.client.resources.BookManagerFabric;
import juuxel.adorn.client.resources.ColorManagerFabric;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;

public final class AdornResources {
    @Environment(EnvType.CLIENT)
    public static void initClient() {
        var resourceManagerHelper = ResourceManagerHelper.get(PackType.CLIENT_RESOURCES);
        resourceManagerHelper.registerReloadListener(ColorManagerFabric.INSTANCE);
        resourceManagerHelper.registerReloadListener(BookManagerFabric.INSTANCE);
    }
}

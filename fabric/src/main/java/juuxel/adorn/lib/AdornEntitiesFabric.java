package juuxel.adorn.lib;

import juuxel.adorn.client.renderer.InvisibleEntityRenderer;
import juuxel.adorn.entity.AdornEntities;
import juuxel.adorn.entity.SeatEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;

public final class AdornEntitiesFabric {
    public static void init() {
        ServerPlayerEvents.LEAVE.register(SeatEntity::stopSitting);
    }

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        EntityRendererRegistry.register(AdornEntities.SEAT.get(), InvisibleEntityRenderer::new);
    }
}

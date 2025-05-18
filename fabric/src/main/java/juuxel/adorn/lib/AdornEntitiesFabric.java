package juuxel.adorn.lib;

import juuxel.adorn.client.renderer.InvisibleEntityRenderer;
import juuxel.adorn.entity.AdornEntities;
import juuxel.adorn.entity.SeatEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public final class AdornEntitiesFabric {
    public static void init() {
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            server.execute(() -> SeatEntity.stopSitting(handler.player));
        });
    }

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        EntityRendererRegistry.register(AdornEntities.SEAT.get(), InvisibleEntityRenderer::new);
    }
}

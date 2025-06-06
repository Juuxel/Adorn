package juuxel.adorn.platform.forge.client;

import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.client.renderer.ConeEntityRenderer;
import juuxel.adorn.client.renderer.InvisibleEntityRenderer;
import juuxel.adorn.client.renderer.ShelfRenderer;
import juuxel.adorn.client.renderer.TradingStationRenderer;
import juuxel.adorn.entity.AdornEntities;
import juuxel.adorn.platform.forge.client.renderer.KitchenSinkRendererForge;
import juuxel.adorn.platform.neo.client.renderer.ConeFeatureRenderer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public final class AdornRenderers {
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AdornEntities.SEAT.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(AdornEntities.CONE.get(), ConeEntityRenderer::new);
        event.registerBlockEntityRenderer(AdornBlockEntities.TRADING_STATION.get(), TradingStationRenderer::new);
        event.registerBlockEntityRenderer(AdornBlockEntities.SHELF.get(), ShelfRenderer::new);
        event.registerBlockEntityRenderer(forceType(AdornBlockEntities.KITCHEN_SINK.get()), KitchenSinkRendererForge::new);
    }

    @SuppressWarnings("unchecked")
    private static <T extends BlockEntity, U extends T> BlockEntityType<U> forceType(BlockEntityType<T> type) {
        return (BlockEntityType<U>) type;
    }

    public static void registerFeatureRenderers(EntityRenderersEvent.AddLayers event) {
        // Players
        for (var skin : event.getSkins()) {
            if (event.getSkin(skin) instanceof PlayerEntityRenderer renderer) {
                addConeFeatureRenderer(renderer);
            }
        }

        // Non-player bipeds
        for (EntityType<?> entityType : event.getEntityTypes()) {
            if (event.getRenderer(entityType) instanceof BipedEntityRenderer<?, ?> renderer) {
                addConeFeatureRenderer(renderer);
            }
        }
    }

    private static <T extends LivingEntity, M extends BipedEntityModel<T>> void addConeFeatureRenderer(LivingEntityRenderer<T, M> renderer) {
        renderer.addFeature(new ConeFeatureRenderer<>(renderer));
    }
}

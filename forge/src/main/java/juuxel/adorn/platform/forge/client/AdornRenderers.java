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
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.entity.EntityType;
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
            addConeFeatureRendererIfApplicable(event.getSkin(skin));
        }

        // Non-player bipeds
        for (EntityType<?> entityType : event.getEntityTypes()) {
            addConeFeatureRendererIfApplicable(event.getRenderer(entityType));
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addConeFeatureRendererIfApplicable(EntityRenderer<?> renderer) {
        if (renderer instanceof LivingEntityRenderer<?, ?> living && shouldAddConeFeatureRenderer(living)) {
            living.addFeature(new ConeFeatureRenderer<>((FeatureRendererContext) living));
        }
    }

    private static boolean shouldAddConeFeatureRenderer(LivingEntityRenderer<?, ?> renderer) {
        for (FeatureRenderer<?, ?> feature : renderer.features) {
            if (feature instanceof ArmorFeatureRenderer<?, ?, ?>) {
                return true;
            }
        }

        return false;
    }
}

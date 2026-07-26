package juuxel.adorn.platform.forge.client;

import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.client.renderer.ConeEntityRenderer;
import juuxel.adorn.client.renderer.InvisibleEntityRenderer;
import juuxel.adorn.client.renderer.KitchenSinkRenderer;
import juuxel.adorn.client.renderer.ShelfRenderer;
import juuxel.adorn.client.renderer.TradingStationRenderer;
import juuxel.adorn.entity.AdornEntities;
import juuxel.adorn.platform.neo.client.renderer.ConeFeatureRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public final class AdornRenderers {
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AdornEntities.SEAT.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer(AdornEntities.CONE.get(), ConeEntityRenderer::new);
        event.registerBlockEntityRenderer(AdornBlockEntities.TRADING_STATION.get(), TradingStationRenderer::new);
        event.registerBlockEntityRenderer(AdornBlockEntities.SHELF.get(), ShelfRenderer::new);
        event.registerBlockEntityRenderer(AdornBlockEntities.KITCHEN_SINK.get(), KitchenSinkRenderer::new);
    }

    @SuppressWarnings("unchecked")
    private static <T extends BlockEntity, U extends T> BlockEntityType<U> forceType(BlockEntityType<T> type) {
        return (BlockEntityType<U>) type;
    }

    public static void registerFeatureRenderers(EntityRenderersEvent.AddLayers event) {
        // Players
        for (var skin : event.getSkins()) {
            addConeFeatureRendererIfApplicable(event.getPlayerRenderer(skin));
        }

        // Non-player bipeds
        for (EntityType<?> entityType : event.getEntityTypes()) {
            addConeFeatureRendererIfApplicable(event.getRenderer(entityType));
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addConeFeatureRendererIfApplicable(EntityRenderer<?, ?> renderer) {
        if (renderer instanceof LivingEntityRenderer<?, ?, ?> living && shouldAddConeFeatureRenderer(living)) {
            living.addLayer(new ConeFeatureRenderer<>((RenderLayerParent) living));
        }
    }

    private static boolean shouldAddConeFeatureRenderer(LivingEntityRenderer<?, ?, ?> renderer) {
        for (RenderLayer<?, ?> feature : renderer.layers) {
            if (feature instanceof HumanoidArmorLayer<?, ?, ?>) {
                // Assumes that the state is a subtype of BipedEntityRenderState
                // if the armor feature is able to be on the entity renderer.
                return true;
            }
        }

        return false;
    }
}

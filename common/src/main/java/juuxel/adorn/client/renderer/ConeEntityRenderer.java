package juuxel.adorn.client.renderer;

import juuxel.adorn.client.CustomModelKeys;
import juuxel.adorn.client.ModelBridge;
import juuxel.adorn.entity.ConeEntity;
import juuxel.adorn.entity.ConeVariant;
import juuxel.adorn.lib.registry.AdornRegistryKeys;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;

import java.util.Optional;

public final class ConeEntityRenderer extends EntityRenderer<ConeEntity, ConeEntityRenderState> {
    private final BakedModelManager modelManager;

    public ConeEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.modelManager = context.getModelManager();
    }

    @Override
    public void render(ConeEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.translate(-0.5f, 0, -0.5f);
        var model = ModelBridge.get().getModel(modelManager, CustomModelKeys.CONES.getEager(state.variant));
        var matrix = matrices.peek();
        var vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE));
        BlockModelRenderer.render(matrix, vertexConsumer, model, 1, 1, 1, light, OverlayTexture.DEFAULT_UV);
        matrices.pop();
        super.render(state, matrices, vertexConsumers, light);
    }

    @Override
    public ConeEntityRenderState createRenderState() {
        return new ConeEntityRenderState();
    }

    @Override
    public void updateRenderState(ConeEntity entity, ConeEntityRenderState state, float tickProgress) {
        super.updateRenderState(entity, state, tickProgress);
        state.variant = getEffectiveVariant(entity.getRegistryManager(), entity).orElse(ConeVariant.Keys.ORANGE);
    }

    private static Optional<RegistryKey<ConeVariant>> getEffectiveVariant(DynamicRegistryManager registryManager, ConeEntity entity) {
        var registry = registryManager.getOrThrow(AdornRegistryKeys.CONE_VARIANT);
        var current = entity.getVariant();

        while (current.value().appearance().isPresent()) {
            var appearance = registry.getOptional(current.value().appearance().get());
            if (appearance.isEmpty()) return Optional.empty();
            current = appearance.get();
        }

        return current.getKey();
    }
}

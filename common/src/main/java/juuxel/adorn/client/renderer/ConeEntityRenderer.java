package juuxel.adorn.client.renderer;

import juuxel.adorn.client.CustomModelKeys;
import juuxel.adorn.client.ModelBridge;
import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.entity.ConeEntity;
import juuxel.adorn.entity.ConeVariant;
import juuxel.adorn.lib.registry.AdornRegistryKeys;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Optional;

public final class ConeEntityRenderer extends EntityRenderer<ConeEntity, ConeEntityRenderState> {
    private static final float SIZE = 0.8f;
    private final BakedModelManager modelManager;

    public ConeEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.modelManager = context.getBlockRenderManager().getModels().getModelManager();
        this.shadowRadius = 0.5f;
    }

    @Override
    public void render(ConeEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        matrices.push();
        matrices.translate(-0.5f, 0, -0.5f);
        queue.submitBlockStateModel(matrices, RenderLayer.getEntityCutout(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE), state.coneModel, 1, 1, 1, state.light, OverlayTexture.DEFAULT_UV, state.outlineColor);
        matrices.pop();
        super.render(state, matrices, queue, cameraState);
    }

    @Override
    public ConeEntityRenderState createRenderState() {
        return new ConeEntityRenderState();
    }

    @Override
    public void updateRenderState(ConeEntity entity, ConeEntityRenderState state, float tickProgress) {
        super.updateRenderState(entity, state, tickProgress);
        RegistryKey<ConeVariant> variant = getEffectiveVariant(entity.getRegistryManager(), entity.getVariant()).orElse(ConeVariant.Keys.ORANGE);
        state.coneModel = getModel(modelManager, variant);
    }

    private static BlockStateModel getModel(BakedModelManager modelManager, RegistryKey<ConeVariant> variant) {
        return ModelBridge.get().getModel(modelManager, CustomModelKeys.CONES.getEager(variant));
    }

    public static void renderOnHead(MatrixStack matrices, OrderedRenderCommandQueue queue, ItemStack stack, int light, int outline, BipedEntityModel<?> contextModel) {
        // Resolve cone variant from stack
        var client = MinecraftClient.getInstance();
        var registryManager = client.world.getRegistryManager();
        var variant = getEffectiveVariant(registryManager, stack).orElse(ConeVariant.Keys.ORANGE);
        var modelManager = client.getBakedModelManager();
        var model = getModel(modelManager, variant);

        RenderLayer renderLayer = RenderLayer.getArmorCutoutNoCull(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
        RenderLayer glintLayer = RenderLayer.getArmorEntityGlint();
        matrices.push();
        contextModel.getRootPart().applyTransform(matrices);
        contextModel.getHead().applyTransform(matrices);
        float headSize = getHeadHeight(matrices, contextModel);
        matrices.translate(SIZE * 0.5f, -headSize, SIZE * 0.5f);
        matrices.scale(-SIZE, -SIZE, -SIZE);
        // TODO: Lighting is reversed (bright side <-> dark side)
        queue.getBatchingQueue(0).submitBlockStateModel(matrices, renderLayer, model, 1, 1, 1, light, OverlayTexture.DEFAULT_UV, outline);
        if (stack.hasGlint()) {
            // TODO: Figure out why the glint doesn't render
            queue.getBatchingQueue(1).submitBlockStateModel(matrices, glintLayer, model, 1, 1, 1, light, OverlayTexture.DEFAULT_UV, outline);
        }
        matrices.pop();
    }

    private static Optional<RegistryKey<ConeVariant>> getEffectiveVariant(DynamicRegistryManager registryManager, ItemStack stack) {
        var variantComponent = stack.get(AdornComponentTypes.CONE_VARIANT.get());
        if (variantComponent == null) return Optional.empty();
        var variant = variantComponent.getVariant(registryManager).orElse(null);
        if (variant == null) return Optional.empty();
        return getEffectiveVariant(registryManager, variant);
    }

    private static Optional<RegistryKey<ConeVariant>> getEffectiveVariant(DynamicRegistryManager registryManager, RegistryEntry<ConeVariant> variant) {
        var registry = registryManager.getOrThrow(AdornRegistryKeys.CONE_VARIANT);

        while (variant.value().appearance().isPresent()) {
            var appearance = registry.getOptional(variant.value().appearance().get());
            if (appearance.isEmpty()) return Optional.empty();
            variant = appearance.get();
        }

        return variant.getKey();
    }

    private static float getHeadHeight(MatrixStack matrices, BipedEntityModel<?> model) {
        var finder = new HeadHeightFinder();
        model.head.forEachCuboid(matrices, finder);
        return finder.headHeight;
    }

    private static final class HeadHeightFinder implements ModelPart.CuboidConsumer {
        private float headHeight;

        @Override
        public void accept(MatrixStack.Entry matrix, String path, int index, ModelPart.Cuboid cuboid) {
            if (headHeight == 0) headHeight = (cuboid.maxY - cuboid.minY) / 16f;
        }
    }
}

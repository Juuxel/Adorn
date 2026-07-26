package juuxel.adorn.client.renderer;

import juuxel.adorn.client.CustomModelKeys;
import juuxel.adorn.client.ModelBridge;
import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.entity.ConeEntity;
import juuxel.adorn.entity.ConeVariant;
import juuxel.adorn.lib.registry.AdornRegistryKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Holder;

import java.util.List;
import java.util.Optional;

public final class ConeEntityRenderer extends EntityRenderer<ConeEntity, ConeEntityRenderState> {
    private static final float SIZE = 0.8f;
    private final ModelManager modelManager;

    public ConeEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.modelManager = context.getBlockRenderDispatcher().getBlockModelShaper().getModelManager();
        this.shadowRadius = 0.5f;
    }

    @Override
    public void submit(ConeEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        matrices.pushPose();
        matrices.translate(-0.5f, 0, -0.5f);
        queue.submitBlockModel(matrices, RenderTypes.entityCutout(TextureAtlas.LOCATION_BLOCKS), state.coneModel, 1, 1, 1, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        matrices.popPose();
        super.submit(state, matrices, queue, cameraState);
    }

    @Override
    public ConeEntityRenderState createRenderState() {
        return new ConeEntityRenderState();
    }

    @Override
    public void extractRenderState(ConeEntity entity, ConeEntityRenderState state, float tickProgress) {
        super.extractRenderState(entity, state, tickProgress);
        ResourceKey<ConeVariant> variant = getEffectiveVariant(entity.registryAccess(), entity.getVariant()).orElse(ConeVariant.Keys.ORANGE);
        state.coneModel = getModel(modelManager, variant);
    }

    private static BlockStateModel getModel(ModelManager modelManager, ResourceKey<ConeVariant> variant) {
        return ModelBridge.get().getModel(modelManager, CustomModelKeys.CONES.getEager(variant));
    }

    public static void renderOnHead(PoseStack matrices, SubmitNodeCollector queue, ItemStack stack, int light, int outline, HumanoidModel<?> contextModel) {
        // Resolve cone variant from stack
        var client = Minecraft.getInstance();
        var registryManager = client.level.registryAccess();
        var variant = getEffectiveVariant(registryManager, stack).orElse(ConeVariant.Keys.ORANGE);
        var modelManager = client.getModelManager();
        var model = getModel(modelManager, variant);

        List<RenderType> renderLayers = ItemRenderer.getFoilRenderTypes(Sheets.solidBlockSheet(), false, stack.hasFoil());
        matrices.pushPose();
        contextModel.root().translateAndRotate(matrices);
        contextModel.getHead().translateAndRotate(matrices);
        float headSize = getHeadHeight(matrices, contextModel);
        matrices.scale(-1, -1, 1);
        matrices.translate(-SIZE * 0.5f, headSize, -SIZE * 0.5f);
        matrices.scale(SIZE, SIZE, SIZE);
        for (int i = 0; i < renderLayers.size(); i++) {
            queue.order(i).submitBlockModel(matrices, renderLayers.get(i), model, 1, 1, 1, light, OverlayTexture.NO_OVERLAY, outline);
        }
        matrices.popPose();
    }

    private static Optional<ResourceKey<ConeVariant>> getEffectiveVariant(RegistryAccess registryManager, ItemStack stack) {
        var variantComponent = stack.get(AdornComponentTypes.CONE_VARIANT.get());
        if (variantComponent == null) return Optional.empty();
        var variant = variantComponent.getVariant(registryManager).orElse(null);
        if (variant == null) return Optional.empty();
        return getEffectiveVariant(registryManager, variant);
    }

    private static Optional<ResourceKey<ConeVariant>> getEffectiveVariant(RegistryAccess registryManager, Holder<ConeVariant> variant) {
        var registry = registryManager.lookupOrThrow(AdornRegistryKeys.CONE_VARIANT);

        while (variant.value().appearance().isPresent()) {
            var appearance = registry.get(variant.value().appearance().get());
            if (appearance.isEmpty()) return Optional.empty();
            variant = appearance.get();
        }

        return variant.unwrapKey();
    }

    private static float getHeadHeight(PoseStack matrices, HumanoidModel<?> model) {
        var finder = new HeadHeightFinder();
        model.head.visit(matrices, finder);
        return finder.headHeight;
    }

    private static final class HeadHeightFinder implements ModelPart.Visitor {
        private float headHeight;

        @Override
        public void visit(PoseStack.Pose matrix, String path, int index, ModelPart.Cube cuboid) {
            if (headHeight == 0) headHeight = (cuboid.maxY - cuboid.minY) / 16f;
        }
    }
}

package juuxel.adorn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import juuxel.adorn.client.CustomModelKeys;
import juuxel.adorn.client.ModelBridge;
import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.entity.ConeEntity;
import juuxel.adorn.entity.ConeVariant;
import juuxel.adorn.lib.registry.AdornRegistryKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.block.model.BlockStateModelWrapper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.feature.ItemFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

import java.util.List;
import java.util.Optional;

public final class ConeEntityRenderer extends EntityRenderer<ConeEntity, ConeEntityRenderState> {
    private static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
    private static final Matrix4fc IDENTITY_MATRIX = new Matrix4f();
    private static final RenderType DEFAULT_RENDER_TYPE = Sheets.cutoutBlockSheet();
    private static final List<RenderType> RENDER_TYPES_WTIHOUT_FOIL = List.of(DEFAULT_RENDER_TYPE);
    private static final List<RenderType> RENDER_TYPES_WITH_FOIL = List.of(DEFAULT_RENDER_TYPE, ItemFeatureRenderer.getFoilRenderType(DEFAULT_RENDER_TYPE, false));
    private static final float SIZE = 0.8f;
    private final ModelManager modelManager;

    public ConeEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.modelManager = context.getBlockModelResolver().modelManager;
        this.shadowRadius = 0.5f;
    }

    @Override
    public void submit(ConeEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        matrices.pushPose();
        matrices.translate(-0.5f, 0, -0.5f);
        state.coneModel.submit(matrices, queue, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
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
        setupBlockModelRenderState(entity.registryAccess(), modelManager, entity.getVariant(), state.coneModel);
    }

    private static BlockStateModel getModel(ModelManager modelManager, ResourceKey<ConeVariant> variant) {
        return ModelBridge.get().getModel(modelManager, CustomModelKeys.CONES.getEager(variant));
    }

    public static void renderOnHead(PoseStack matrices, SubmitNodeCollector queue, ItemStack stack, int light, int outline, HumanoidModel<?> contextModel) {
        // Resolve cone variant from stack
        var client = Minecraft.getInstance();
        var registryManager = client.level.registryAccess();
        var variant = stack.get(AdornComponentTypes.CONE_VARIANT.get());
        var modelRenderState = new BlockModelRenderState();
        setupBlockModelRenderState(registryManager, client.getModelManager(), variant, modelRenderState);

        List<RenderType> renderLayers = getFoilRenderTypes(stack.hasFoil());
        matrices.pushPose();
        contextModel.root().translateAndRotate(matrices);
        contextModel.getHead().translateAndRotate(matrices);
        float headSize = getHeadHeight(matrices, contextModel);
        matrices.scale(-1, -1, 1);
        matrices.translate(-SIZE * 0.5f, headSize, -SIZE * 0.5f);
        matrices.scale(SIZE, SIZE, SIZE);
        for (int i = 0; i < renderLayers.size(); i++) {
            modelRenderState.submit(matrices, queue, light, OverlayTexture.NO_OVERLAY, outline);
        }
        matrices.popPose();
    }

    private static void setupBlockModelRenderState(RegistryAccess registryAccess, ModelManager modelManager, @Nullable Holder<ConeVariant> variant, BlockModelRenderState state) {
        ResourceKey<ConeVariant> variantKey = ConeVariant.Keys.ORANGE;

        if (variant != null) {
            var effective = getEffectiveVariant(registryAccess, variant).orElse(null);
            if (effective != null) variantKey = effective;
        }

        var model = getModel(modelManager, variantKey);
        var modelWrapper = new BlockStateModelWrapper(model, List.of(), IDENTITY_MATRIX);
        modelWrapper.update(state, Blocks.AIR.defaultBlockState(), BLOCK_DISPLAY_CONTEXT, 42);
    }

    // TODO (26.1): Fix glint not rendering once again
    private static List<RenderType> getFoilRenderTypes(boolean hasFoil) {
        return hasFoil ? RENDER_TYPES_WITH_FOIL : RENDER_TYPES_WTIHOUT_FOIL;
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

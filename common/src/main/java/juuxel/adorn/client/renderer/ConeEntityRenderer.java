package juuxel.adorn.client.renderer;

import juuxel.adorn.client.CustomModelKeys;
import juuxel.adorn.client.ModelBridge;
import juuxel.adorn.entity.ConeEntity;
import juuxel.adorn.entity.ConeVariant;
import juuxel.adorn.item.ConeItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public final class ConeEntityRenderer extends EntityRenderer<ConeEntity> {
    private static final float SIZE = 0.8f;
    private final BakedModelManager modelManager;

    public ConeEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.modelManager = context.getModelManager();
        this.shadowRadius = 0.5f;
    }

    @Override
    public Identifier getTexture(ConeEntity entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }

    @Override
    public void render(ConeEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.translate(-0.5f, 0, -0.5f);
        renderCone(modelManager, entity.getVariant(), matrices, vertexConsumers, light, false);
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    private static void renderCone(BakedModelManager modelManager, ConeVariant variant, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, boolean glint) {
        var model = ModelBridge.get().getModel(modelManager, CustomModelKeys.CONES.getEager(variant));
        var matrix = matrices.peek();
        var vertexConsumer = ItemRenderer.getItemGlintConsumer(vertexConsumers, TexturedRenderLayers.getEntitySolid(), false, glint);
        BlockModelRenderer modelRenderer = MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer();
        modelRenderer.render(matrix, vertexConsumer, null, model, 1, 1, 1, light, OverlayTexture.DEFAULT_UV);
    }

    public static void renderOnHead(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, int light, BipedEntityModel<?> contextModel) {
        var variant = ((ConeItem) stack.getItem()).getVariant();
        var modelManager = MinecraftClient.getInstance().getBakedModelManager();
        matrices.push();
        contextModel.getHead().rotate(matrices);
        float headSize = getHeadHeight(matrices, contextModel);
        matrices.scale(-1, -1, 1);
        matrices.translate(-SIZE * 0.5f, headSize, -SIZE * 0.5f);
        matrices.scale(SIZE, SIZE, SIZE);
        renderCone(modelManager, variant, matrices, vertexConsumers, light, stack.hasGlint());
        matrices.pop();
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

package juuxel.adorn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import juuxel.adorn.block.AbstractKitchenCounterBlock;
import juuxel.adorn.block.entity.KitchenSinkBlockEntity;
import juuxel.adorn.client.FluidRenderingBridge;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.util.Logging;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.data.AtlasIds;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public final class KitchenSinkRenderer implements BlockEntityRenderer<KitchenSinkBlockEntity, KitchenSinkRenderState> {
    private static final Logger LOGGER = Logging.logger();
    private static final float PX = 1 / 16f;
    private static final float X_START = 2 * PX;
    private static final float X_END = 13 * PX;
    private static final float Z_START = 2 * PX;
    private static final float Z_END = 14 * PX;
    private static final float Y_START = 7 * PX;
    private static final float Y_END = 15 * PX;
    private static final double LITRES_PER_BLOCK = 1000.0;

    // Wave period in ms
    private static final float WAVE_PERIOD = 12_000f;
    private static final float WAVE_HEIGHT = PX;
    private static final float MS_PER_TICK = 50f;

    private static float getRotation(Direction facing) {
        return switch (facing) {
            case EAST -> 0f;
            case NORTH -> 90f;
            case WEST -> 180f;
            case SOUTH -> 270f;
            // Vertical orientations
            default -> 0f;
        };
    }

    private final FluidRenderWrapper fluidRenderWrapper = new FluidRenderWrapper();

    public KitchenSinkRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public KitchenSinkRenderState createRenderState() {
        return new KitchenSinkRenderState();
    }

    @Override
    public void extractRenderState(KitchenSinkBlockEntity blockEntity, KitchenSinkRenderState state, float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.fluid = blockEntity.getFluidReference().createSnapshot();
        state.fluidColor = getFluidColor(blockEntity);
        state.animationTime = (float) ((double) System.currentTimeMillis() % WAVE_PERIOD / MS_PER_TICK);
        state.tickProgress = tickProgress;
    }

    @Override
    public void submit(KitchenSinkRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        // Skip if there's nothing to render
        if (state.fluid.isEmpty()) return;

        matrices.pushPose();
        // Rotate because the model depends on the facing property
        matrices.translate(0.5, 0.0, 0.5);
        matrices.mulPose(Axis.YP.rotationDegrees(getRotation(state.blockState.getValue(AbstractKitchenCounterBlock.FACING))));
        matrices.translate(-0.5, 0.0, -0.5);

        // Move vertically to correct level
        double fluidLevel = getFluidLevel(state) / LITRES_PER_BLOCK;
        matrices.translate(0.0, Mth.lerp(fluidLevel, Y_START, Y_END), 0.0);

        fluidRenderWrapper.renderState = state;
        var sprite = getFluidSprite(state);
        fluidRenderWrapper.sprite = sprite;
        queue.submitCustomGeometry(matrices, RenderTypes.entityTranslucent(sprite.atlasLocation()), fluidRenderWrapper);

        matrices.popPose();
    }

    private void renderFluid(KitchenSinkRenderState state, TextureAtlasSprite sprite, PoseStack.Pose matrixEntry, VertexConsumer vertexConsumer) {
        float u0 = Mth.lerp(2 * PX, sprite.getU0(), sprite.getU1());
        float u1 = Mth.lerp(14 * PX, sprite.getU0(), sprite.getU1());
        float v0 = Mth.lerp(2 * PX, sprite.getV0(), sprite.getV1());
        float v1 = Mth.lerp(13 * PX, sprite.getV0(), sprite.getV1());

        var positionMatrix = matrixEntry.pose();
        var color = state.fluidColor;
        vertexConsumer.addVertex(positionMatrix, X_START, computeY(state, X_START, Z_END), Z_END)
            .setColor(color).setUv(u0, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(state.lightCoords).setNormal(matrixEntry, 0f, 1f, 0f);
        vertexConsumer.addVertex(positionMatrix, X_END, computeY(state, X_END, Z_END), Z_END)
            .setColor(color).setUv(u0, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(state.lightCoords).setNormal(matrixEntry, 0f, 1f, 0f);
        vertexConsumer.addVertex(positionMatrix, X_END, computeY(state, X_END, Z_START), Z_START)
            .setColor(color).setUv(u1, v1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(state.lightCoords).setNormal(matrixEntry, 0f, 1f, 0f);
        vertexConsumer.addVertex(positionMatrix, X_START, computeY(state, X_START, Z_START), Z_START)
            .setColor(color).setUv(u1, v0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(state.lightCoords).setNormal(matrixEntry, 0f, 1f, 0f);
    }

    private static float computeY(KitchenSinkRenderState state, float x, float z) {
        var time = ((state.animationTime + state.tickProgress) * MS_PER_TICK) % WAVE_PERIOD;
        var t = time * Mth.TWO_PI / WAVE_PERIOD;
        return Mth.sin(t + x + z) * WAVE_HEIGHT / 2;
    }

    private TextureAtlasSprite getFluidSprite(KitchenSinkRenderState state) {
        var sprite = FluidRenderingBridge.get().getStillSprite(state.fluid);

        if (sprite == null) {
            LOGGER.error("Could not find sprite for fluid reference {} when rendering kitchen sink at {}", state.fluid, state.blockPos);
            return Minecraft.getInstance()
                .getAtlasManager()
                .getAtlasOrThrow(AtlasIds.BLOCKS)
                .missingSprite();
        }

        return sprite;
    }

    /** Gets the entity's fluid's color. */
    private int getFluidColor(KitchenSinkBlockEntity entity) {
        return FluidRenderingBridge.get().getColor(entity.getFluidReference(), (ClientLevel) entity.getLevel(), entity.getBlockPos());
    }

    /** Gets the fluid level from the entity in litres. */
    private double getFluidLevel(KitchenSinkRenderState state) {
        return FluidUnit.convertAsDouble(state.fluid.getAmount(), state.fluid.getUnit(), FluidUnit.LITRE);
    }

    private final class FluidRenderWrapper implements SubmitNodeCollector.CustomGeometryRenderer {
        private KitchenSinkRenderState renderState;
        private TextureAtlasSprite sprite;

        @Override
        public void render(PoseStack.Pose matricesEntry, VertexConsumer vertexConsumer) {
            renderFluid(renderState, sprite, matricesEntry, vertexConsumer);
        }
    }
}

package juuxel.adorn.client.renderer;

import juuxel.adorn.block.AbstractKitchenCounterBlock;
import juuxel.adorn.block.entity.KitchenSinkBlockEntity;
import juuxel.adorn.client.FluidRenderingBridge;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.util.Logging;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Atlases;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
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

    public KitchenSinkRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public KitchenSinkRenderState createRenderState() {
        return new KitchenSinkRenderState();
    }

    @Override
    public void updateRenderState(KitchenSinkBlockEntity blockEntity, KitchenSinkRenderState state, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.fluid = blockEntity.getFluidReference().createSnapshot();
        state.fluidColor = getFluidColor(blockEntity);
        state.animationTime = (float) ((double) System.currentTimeMillis() % WAVE_PERIOD / MS_PER_TICK);
        state.tickProgress = tickProgress;
    }

    @Override
    public void render(KitchenSinkRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        // Skip if there's nothing to render
        if (state.fluid.isEmpty()) return;

        matrices.push();
        // Rotate because the model depends on the facing property
        matrices.translate(0.5, 0.0, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(getRotation(state.blockState.get(AbstractKitchenCounterBlock.FACING))));
        matrices.translate(-0.5, 0.0, -0.5);

        // Move vertically to correct level
        double fluidLevel = getFluidLevel(state) / LITRES_PER_BLOCK;
        matrices.translate(0.0, MathHelper.lerp(fluidLevel, Y_START, Y_END), 0.0);

        fluidRenderWrapper.renderState = state;
        var sprite = getFluidSprite(state);
        fluidRenderWrapper.sprite = sprite;
        queue.submitCustom(matrices, RenderLayers.entityTranslucent(sprite.getAtlasId()), fluidRenderWrapper);

        matrices.pop();
    }

    private void renderFluid(KitchenSinkRenderState state, Sprite sprite, MatrixStack.Entry matrixEntry, VertexConsumer vertexConsumer) {
        float u0 = MathHelper.lerp(2 * PX, sprite.getMinU(), sprite.getMaxU());
        float u1 = MathHelper.lerp(14 * PX, sprite.getMinU(), sprite.getMaxU());
        float v0 = MathHelper.lerp(2 * PX, sprite.getMinV(), sprite.getMaxV());
        float v1 = MathHelper.lerp(13 * PX, sprite.getMinV(), sprite.getMaxV());

        var positionMatrix = matrixEntry.getPositionMatrix();
        var color = state.fluidColor;
        vertexConsumer.vertex(positionMatrix, X_START, computeY(state, X_START, Z_END), Z_END)
            .color(color).texture(u0, v0).overlay(OverlayTexture.DEFAULT_UV).light(state.lightmapCoordinates).normal(matrixEntry, 0f, 1f, 0f);
        vertexConsumer.vertex(positionMatrix, X_END, computeY(state, X_END, Z_END), Z_END)
            .color(color).texture(u0, v1).overlay(OverlayTexture.DEFAULT_UV).light(state.lightmapCoordinates).normal(matrixEntry, 0f, 1f, 0f);
        vertexConsumer.vertex(positionMatrix, X_END, computeY(state, X_END, Z_START), Z_START)
            .color(color).texture(u1, v1).overlay(OverlayTexture.DEFAULT_UV).light(state.lightmapCoordinates).normal(matrixEntry, 0f, 1f, 0f);
        vertexConsumer.vertex(positionMatrix, X_START, computeY(state, X_START, Z_START), Z_START)
            .color(color).texture(u1, v0).overlay(OverlayTexture.DEFAULT_UV).light(state.lightmapCoordinates).normal(matrixEntry, 0f, 1f, 0f);
    }

    private static float computeY(KitchenSinkRenderState state, float x, float z) {
        var time = ((state.animationTime + state.tickProgress) * MS_PER_TICK) % WAVE_PERIOD;
        var t = time * MathHelper.TAU / WAVE_PERIOD;
        return MathHelper.sin(t + x + z) * WAVE_HEIGHT / 2;
    }

    private Sprite getFluidSprite(KitchenSinkRenderState state) {
        var sprite = FluidRenderingBridge.get().getStillSprite(state.fluid);

        if (sprite == null) {
            LOGGER.error("Could not find sprite for fluid reference {} when rendering kitchen sink at {}", state.fluid, state.pos);
            return MinecraftClient.getInstance()
                .getAtlasManager()
                .getAtlasTexture(Atlases.BLOCKS)
                .getMissingSprite();
        }

        return sprite;
    }

    /** Gets the entity's fluid's color. */
    private int getFluidColor(KitchenSinkBlockEntity entity) {
        return FluidRenderingBridge.get().getColor(entity.getFluidReference(), entity.getWorld(), entity.getPos());
    }

    /** Gets the fluid level from the entity in litres. */
    private double getFluidLevel(KitchenSinkRenderState state) {
        return FluidUnit.convertAsDouble(state.fluid.getAmount(), state.fluid.getUnit(), FluidUnit.LITRE);
    }

    private final class FluidRenderWrapper implements OrderedRenderCommandQueue.Custom {
        private KitchenSinkRenderState renderState;
        private Sprite sprite;

        @Override
        public void render(MatrixStack.Entry matricesEntry, VertexConsumer vertexConsumer) {
            renderFluid(renderState, sprite, matricesEntry, vertexConsumer);
        }
    }
}

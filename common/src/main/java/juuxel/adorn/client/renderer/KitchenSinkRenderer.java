package juuxel.adorn.client.renderer;

import juuxel.adorn.block.AbstractKitchenCounterBlock;
import juuxel.adorn.block.entity.KitchenSinkBlockEntity;
import juuxel.adorn.client.FluidRenderingBridge;
import juuxel.adorn.util.Logging;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;

public abstract class KitchenSinkRenderer<T extends KitchenSinkBlockEntity> implements BlockEntityRenderer<T> {
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

    protected KitchenSinkRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(T entity, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos) {
        // Skip if there's nothing to render
        if (isEmpty(entity)) return;

        var sprite = getFluidSprite(entity);
        var buffer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(sprite.getAtlasId()));
        float u0 = MathHelper.lerp(2 * PX, sprite.getMinU(), sprite.getMaxU());
        float u1 = MathHelper.lerp(14 * PX, sprite.getMinU(), sprite.getMaxU());
        float v0 = MathHelper.lerp(2 * PX, sprite.getMinV(), sprite.getMaxV());
        float v1 = MathHelper.lerp(13 * PX, sprite.getMinV(), sprite.getMaxV());

        matrices.push();
        // Rotate because the model depends on the facing property
        matrices.translate(0.5, 0.0, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(getRotation(entity.getCachedState().get(AbstractKitchenCounterBlock.FACING))));
        matrices.translate(-0.5, 0.0, -0.5);

        // Move vertically to correct level
        double fluidLevel = getFluidLevel(entity) / LITRES_PER_BLOCK;
        matrices.translate(0.0, MathHelper.lerp(fluidLevel, Y_START, Y_END), 0.0);

        // Draw the sprite

        var matrixEntry = matrices.peek();
        var positionMatrix = matrixEntry.getPositionMatrix();
        var color = getFluidColor(entity);
        buffer.vertex(positionMatrix, X_START, computeY(X_START, Z_END), Z_END)
            .color(color).texture(u0, v0).overlay(overlay).light(light).normal(matrixEntry, 0f, 1f, 0f);
        buffer.vertex(positionMatrix, X_END, computeY(X_END, Z_END), Z_END)
            .color(color).texture(u0, v1).overlay(overlay).light(light).normal(matrixEntry, 0f, 1f, 0f);
        buffer.vertex(positionMatrix, X_END, computeY(X_END, Z_START), Z_START)
            .color(color).texture(u1, v1).overlay(overlay).light(light).normal(matrixEntry, 0f, 1f, 0f);
        buffer.vertex(positionMatrix, X_START, computeY(X_START, Z_START), Z_START)
            .color(color).texture(u1, v0).overlay(overlay).light(light).normal(matrixEntry, 0f, 1f, 0f);
        matrices.pop();
    }

    private static float computeY(float x, float z) {
        var time = (MinecraftClient.getInstance().player.age * MS_PER_TICK) % WAVE_PERIOD;
        var t = time * MathHelper.TAU / WAVE_PERIOD;
        return MathHelper.sin(t + x + z) * WAVE_HEIGHT / 2;
    }

    private Sprite getFluidSprite(T entity) {
        var sprite = FluidRenderingBridge.get().getStillSprite(entity.getFluidReference());

        if (sprite == null) {
            LOGGER.error("Could not find sprite for fluid reference {} when rendering kitchen sink at {}", entity.getFluidReference(), entity.getPos());
            return MinecraftClient.getInstance()
                .getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
                .apply(MissingSprite.getMissingSpriteId());
        }

        return sprite;
    }

    /** Gets the entity's fluid's color. */
    private int getFluidColor(T entity) {
        return FluidRenderingBridge.get().getColor(entity.getFluidReference(), entity.getWorld(), entity.getPos());
    }

    /** Gets the fluid level from the entity in litres. */
    protected abstract double getFluidLevel(T entity);

    /** Tests whether the [entity] has no fluid inside. */
    protected abstract boolean isEmpty(T entity);
}

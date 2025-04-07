package juuxel.adorn.client.renderer;

import juuxel.adorn.block.ShelfBlock;
import juuxel.adorn.block.entity.ShelfBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

public final class ShelfRenderer implements BlockEntityRenderer<ShelfBlockEntity> {
    private static final float ITEM_SCALE = 0.5f;
    private static final float ITEM_1_Y_ROT = 10f;
    private static final float ITEM_2_Y_ROT = -17f;

    public ShelfRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(ShelfBlockEntity be, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos) {
        Direction facing = be.getCachedState().get(ShelfBlock.FACING);

        // For first item
        double tx1 = switch (facing) {
            case SOUTH, WEST -> 12 / 16.0;
            default -> 4 / 16.0;
        };
        double tz1 = switch (facing) {
            case NORTH, WEST -> 12 / 16.0;
            default -> 4 / 16.0;
        };

        // For second item
        double tx2 = switch (facing) {
            case NORTH, WEST -> 12 / 16.0;
            default -> 4 / 16.0;
        };
        double tz2 = switch (facing) {
            case NORTH, EAST -> 12 / 16.0;
            default -> 4 / 16.0;
        };

        var itemRenderer = MinecraftClient.getInstance().getItemRenderer();

        matrices.push();
        matrices.translate(tx1, 9.6 / 16.0, tz1);
        matrices.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(ITEM_1_Y_ROT + 180 - facing.getPositiveHorizontalDegrees()));
        itemRenderer.renderItem(
            be.getStack(0),
            ItemDisplayContext.FIXED,
            light,
            overlay,
            matrices,
            vertexConsumers,
            be.getWorld(),
            0 // seed
        );
        matrices.pop();

        matrices.push();
        matrices.translate(tx2, 9.6 / 16.0, tz2);
        matrices.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(ITEM_2_Y_ROT + 180 - facing.getPositiveHorizontalDegrees()));
        itemRenderer.renderItem(
            be.getStack(1),
            ItemDisplayContext.FIXED,
            light,
            overlay,
            matrices,
            vertexConsumers,
            be.getWorld(),
            0 // seed
        );
        matrices.pop();
    }
}

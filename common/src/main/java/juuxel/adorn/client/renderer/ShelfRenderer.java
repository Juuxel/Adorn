package juuxel.adorn.client.renderer;

import juuxel.adorn.block.ShelfBlock;
import juuxel.adorn.block.entity.ShelfBlockEntity;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public final class ShelfRenderer implements BlockEntityRenderer<ShelfBlockEntity, ShelfRenderState> {
    private static final float ITEM_SCALE = 0.5f;
    private static final float ITEM_1_Y_ROT = 10f;
    private static final float ITEM_2_Y_ROT = -17f;

    private final ItemModelManager itemModelManager;

    public ShelfRenderer(BlockEntityRendererFactory.Context context) {
        this.itemModelManager = context.itemModelManager();
    }

    @Override
    public ShelfRenderState createRenderState() {
        return new ShelfRenderState();
    }

    @Override
    public void updateRenderState(ShelfBlockEntity blockEntity, ShelfRenderState state, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);

        int seed = (int) blockEntity.getPos().asLong();

        var first = new ItemRenderState();
        itemModelManager.clearAndUpdate(first, blockEntity.getStack(0), ItemDisplayContext.FIXED, blockEntity.getWorld(), null, seed);
        state.firstItem = first;

        var second = new ItemRenderState();
        itemModelManager.clearAndUpdate(second, blockEntity.getStack(1), ItemDisplayContext.FIXED, blockEntity.getWorld(), null, seed + 1);
        state.secondItem = second;
    }

    @Override
    public void render(ShelfRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        Direction facing = state.blockState.get(ShelfBlock.FACING);

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

        matrices.push();
        matrices.translate(tx1, 9.6 / 16.0, tz1);
        matrices.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(ITEM_1_Y_ROT + 180 - facing.getPositiveHorizontalDegrees()));
        state.firstItem.render(matrices, queue, state.lightmapCoordinates, OverlayTexture.DEFAULT_UV, 0);
        matrices.pop();

        matrices.push();
        matrices.translate(tx2, 9.6 / 16.0, tz2);
        matrices.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(ITEM_2_Y_ROT + 180 - facing.getPositiveHorizontalDegrees()));
        state.secondItem.render(matrices, queue, state.lightmapCoordinates, OverlayTexture.DEFAULT_UV, 0);
        matrices.pop();
    }
}

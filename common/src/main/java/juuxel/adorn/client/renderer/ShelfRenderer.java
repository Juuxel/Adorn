package juuxel.adorn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import juuxel.adorn.block.ShelfBlock;
import juuxel.adorn.block.entity.ShelfBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public final class ShelfRenderer implements BlockEntityRenderer<ShelfBlockEntity, ShelfRenderState> {
    private static final float ITEM_SCALE = 0.5f;
    private static final float ITEM_1_Y_ROT = 10f;
    private static final float ITEM_2_Y_ROT = -17f;

    private final ItemModelResolver itemModelManager;

    public ShelfRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelManager = context.itemModelResolver();
    }

    @Override
    public ShelfRenderState createRenderState() {
        return new ShelfRenderState();
    }

    @Override
    public void extractRenderState(ShelfBlockEntity blockEntity, ShelfRenderState state, float tickProgress, Vec3 cameraPos, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);

        int seed = (int) blockEntity.getBlockPos().asLong();

        var first = new ItemStackRenderState();
        itemModelManager.updateForTopItem(first, blockEntity.getItem(0), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, seed);
        state.firstItem = first;

        var second = new ItemStackRenderState();
        itemModelManager.updateForTopItem(second, blockEntity.getItem(1), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, seed + 1);
        state.secondItem = second;
    }

    @Override
    public void submit(ShelfRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        Direction facing = state.blockState.getValue(ShelfBlock.FACING);

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

        matrices.pushPose();
        matrices.translate(tx1, 9.6 / 16.0, tz1);
        matrices.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        matrices.mulPose(Axis.YP.rotationDegrees(ITEM_1_Y_ROT + 180 - facing.toYRot()));
        state.firstItem.submit(matrices, queue, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        matrices.popPose();

        matrices.pushPose();
        matrices.translate(tx2, 9.6 / 16.0, tz2);
        matrices.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        matrices.mulPose(Axis.YP.rotationDegrees(ITEM_2_Y_ROT + 180 - facing.toYRot()));
        state.secondItem.submit(matrices, queue, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        matrices.popPose();
    }
}

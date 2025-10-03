package juuxel.adorn.client.renderer;

import juuxel.adorn.block.entity.TradingStationBlockEntity;
import juuxel.adorn.config.ConfigManager;
import juuxel.adorn.util.Colors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public final class TradingStationRenderer implements BlockEntityRenderer<TradingStationBlockEntity, TradingStationRenderState> {
    private static final float SELLING_ROTATION_MULTIPLIER = 1.2f;
    private static final double ROTATION_LENGTH_IN_TICKS = 360.0 / SELLING_ROTATION_MULTIPLIER;
    private static final String OWNER_LABEL = "block.adorn.trading_station.label.owner";
    private static final String SELLING_LABEL = "block.adorn.trading_station.label.selling";
    private static final String PRICE_LABEL = "block.adorn.trading_station.label.price";

    private final ItemModelManager itemModelManager;
    private final TextRenderer textRenderer;

    public TradingStationRenderer(BlockEntityRendererFactory.Context context) {
        itemModelManager = context.itemModelManager();
        textRenderer = context.textRenderer();
    }

    @Override
    public TradingStationRenderState createRenderState() {
        return new TradingStationRenderState();
    }

    @Override
    public void updateRenderState(TradingStationBlockEntity blockEntity, TradingStationRenderState state, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.trade.setFrom(blockEntity.getTrade(), itemModelManager, blockEntity.getWorld(), (int) blockEntity.getPos().asLong());
        state.ownerName = blockEntity.getOwnerName().copy().formatted(Formatting.GOLD);
        state.rotationInTicks = (float) (System.currentTimeMillis() / 50.0 % ROTATION_LENGTH_IN_TICKS);
        state.tickProgress = tickProgress;
    }

    @Override
    public void render(TradingStationRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        var lookingAtBlock = cameraState.blockPos.isWithinDistance(state.pos, 3);
        var trade = state.trade;
        int light = state.lightmapCoordinates;

        if (!trade.empty) {
            matrices.push();
            matrices.translate(0.5, 1.2, 0.5);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((state.rotationInTicks + state.tickProgress) * SELLING_ROTATION_MULTIPLIER));
            matrices.scale(0.6f, 0.6f, 0.6f);
            matrices.translate(0.0, 0.3, 0.0);
            trade.selling.render(matrices, queue, light, OverlayTexture.DEFAULT_UV, 0);
            matrices.pop();
        }

        if (lookingAtBlock && ConfigManager.config().client.showTradingStationTooltips) {
            Text label1 = Text.translatable(OWNER_LABEL, state.ownerName);
            renderLabel(state, cameraState, queue, label1, 0.0, 0.9, 0.0, 12, matrices, light);
            if (!trade.empty) {
                Text label2 = Text.translatable(SELLING_LABEL, trade.sellingLabel);
                Text label3 = Text.translatable(PRICE_LABEL, trade.priceLabel);
                renderLabel(state, cameraState, queue, label2, 0.0, 0.9 - 0.25, 0.0, 12, matrices, light);
                renderLabel(state, cameraState, queue, label3, 0.0, 0.9 - 0.5, 0.0, 12, matrices, light);
            }
        }
    }

    private void renderLabel(
        TradingStationRenderState state, CameraRenderState cameraState, OrderedRenderCommandQueue queue,
        Text label, double x, double y, double z,
        int maxDistance, MatrixStack matrices, int light
    ) {
        double dist = state.pos.getSquaredDistanceFromCenter(cameraState.pos.x, cameraState.pos.y, cameraState.pos.z);
        if (dist < maxDistance * maxDistance) {
            matrices.push();
            matrices.translate(x + 0.5, y + 1.5, z + 0.5);
            matrices.multiply(cameraState.orientation);
            matrices.scale(+0.025f, -0.025f, +0.025f);

            float opacity = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25f);
            int backgroundColor = Colors.color(0x000000, opacity);
            var textX = -textRenderer.getWidth(label) * 0.5f;
            OrderedText labelOrdered = label.asOrderedText();
            queue.submitText(matrices, textX, 0f, labelOrdered, false, TextRenderer.TextLayerType.SEE_THROUGH, light, 0x20_FFFFFF, backgroundColor, Colors.TRANSPARENT);
            queue.submitText(matrices, textX, 0f, labelOrdered, false, TextRenderer.TextLayerType.NORMAL, light, Colors.WHITE, Colors.TRANSPARENT, Colors.TRANSPARENT);

            matrices.pop();
        }
    }
}

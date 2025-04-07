package juuxel.adorn.client.renderer;

import juuxel.adorn.block.entity.TradingStationBlockEntity;
import juuxel.adorn.config.ConfigManager;
import juuxel.adorn.util.Colors;
import juuxel.adorn.util.AdornUtil;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

public final class TradingStationRenderer implements BlockEntityRenderer<TradingStationBlockEntity> {
    private static final float SELLING_ROTATION_MULTIPLIER = 1.2f;
    private static final String OWNER_LABEL = "block.adorn.trading_station.label.owner";
    private static final String SELLING_LABEL = "block.adorn.trading_station.label.selling";
    private static final String PRICE_LABEL = "block.adorn.trading_station.label.price";

    private final BlockEntityRenderDispatcher dispatcher;
    private final TextRenderer textRenderer;

    public TradingStationRenderer(BlockEntityRendererFactory.Context context) {
        dispatcher = context.getRenderDispatcher();
        textRenderer = context.getTextRenderer();
    }

    @Override
    public void render(TradingStationBlockEntity be, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos) {
        var hitResult = dispatcher.crosshairTarget;
        var lookingAtBlock = hitResult != null && hitResult.getType() == HitResult.Type.BLOCK && be.getPos().equals(((BlockHitResult) hitResult).getBlockPos());
        var trade = be.getTrade();

        if (!trade.isEmpty()) {
            matrices.push();
            matrices.translate(0.5, 1.2, 0.5);
            int playerAge = MinecraftClient.getInstance().player.age;
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((playerAge + tickDelta) * SELLING_ROTATION_MULTIPLIER));
            matrices.scale(0.6f, 0.6f, 0.6f);
            matrices.translate(0.0, 0.3, 0.0);
            var itemRenderer = MinecraftClient.getInstance().getItemRenderer();
            itemRenderer.renderItem(trade.getSelling(), ItemDisplayContext.FIXED, light, overlay, matrices, vertexConsumers, be.getWorld(), 0);
            matrices.pop();
        }

        if (lookingAtBlock && ConfigManager.config().client.showTradingStationTooltips) {
            Text label1 = Text.translatable(OWNER_LABEL, be.getOwnerName().copy().formatted(Formatting.GOLD));
            renderLabel(be, label1, 0.0, 0.9, 0.0, 12, matrices, vertexConsumers, light);
            if (!be.getTrade().isEmpty()) {
                Text label2 = Text.translatable(SELLING_LABEL, AdornUtil.toTextWithCount(be.getTrade().getSelling()));
                Text label3 = Text.translatable(PRICE_LABEL, AdornUtil.toTextWithCount(be.getTrade().getPrice()));
                renderLabel(be, label2, 0.0, 0.9 - 0.25, 0.0, 12, matrices, vertexConsumers, light);
                renderLabel(be, label3, 0.0, 0.9 - 0.5, 0.0, 12, matrices, vertexConsumers, light);
            }
        }
    }

    private void renderLabel(
        BlockEntity be, Text label, double x, double y, double z,
        int maxDistance, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light
    ) {
        var camera = dispatcher.camera;

        double dist = be.getPos().getSquaredDistanceFromCenter(camera.getPos().x, camera.getPos().y, camera.getPos().z);
        if (dist < maxDistance * maxDistance) {
            matrices.push();
            matrices.translate(x + 0.5, y + 1.5, z + 0.5);
            matrices.multiply(camera.getRotation());
            matrices.scale(+0.025f, -0.025f, +0.025f);

            var positionMatrix = matrices.peek().getPositionMatrix();
            float opacity = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25f);
            int backgroundColor = Colors.color(0x000000, opacity);
            var textX = -textRenderer.getWidth(label) * 0.5f;
            textRenderer.draw(label, textX, 0f, 0x20_FFFFFF, false, positionMatrix, vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH, backgroundColor, light);
            textRenderer.draw(label, textX, 0f, Colors.WHITE, false, positionMatrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, Colors.TRANSPARENT, light);

            matrices.pop();
        }
    }


}

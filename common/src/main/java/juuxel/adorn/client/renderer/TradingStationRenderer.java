package juuxel.adorn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import juuxel.adorn.block.entity.TradingStationBlockEntity;
import juuxel.adorn.config.ConfigManager;
import juuxel.adorn.util.Colors;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public final class TradingStationRenderer implements BlockEntityRenderer<TradingStationBlockEntity, TradingStationRenderState> {
    private static final float SELLING_ROTATION_MULTIPLIER = 1.2f;
    private static final double ROTATION_LENGTH_IN_TICKS = 360.0 / SELLING_ROTATION_MULTIPLIER;
    private static final String OWNER_LABEL = "block.adorn.trading_station.label.owner";
    private static final String SELLING_LABEL = "block.adorn.trading_station.label.selling";
    private static final String PRICE_LABEL = "block.adorn.trading_station.label.price";

    private final ItemModelResolver itemModelManager;
    private final Font textRenderer;

    public TradingStationRenderer(BlockEntityRendererProvider.Context context) {
        itemModelManager = context.itemModelResolver();
        textRenderer = context.font();
    }

    @Override
    public TradingStationRenderState createRenderState() {
        return new TradingStationRenderState();
    }

    @Override
    public void extractRenderState(TradingStationBlockEntity blockEntity, TradingStationRenderState state, float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.trade.setFrom(blockEntity.getTrade(), itemModelManager, blockEntity.getLevel(), (int) blockEntity.getBlockPos().asLong());
        state.ownerName = blockEntity.getOwnerName().copy().withStyle(ChatFormatting.GOLD);
        state.rotationInTicks = (float) (System.currentTimeMillis() / 50.0 % ROTATION_LENGTH_IN_TICKS);
        state.tickProgress = tickProgress;
    }

    @Override
    public void submit(TradingStationRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        var lookingAtBlock = cameraState.blockPos.closerThan(state.blockPos, 3);
        var trade = state.trade;
        int light = state.lightCoords;

        if (!trade.empty) {
            matrices.pushPose();
            matrices.translate(0.5, 1.2, 0.5);
            matrices.mulPose(Axis.YP.rotationDegrees((state.rotationInTicks + state.tickProgress) * SELLING_ROTATION_MULTIPLIER));
            matrices.scale(0.6f, 0.6f, 0.6f);
            matrices.translate(0.0, 0.3, 0.0);
            trade.selling.submit(matrices, queue, light, OverlayTexture.NO_OVERLAY, 0);
            matrices.popPose();
        }

        if (lookingAtBlock && ConfigManager.config().client.showTradingStationTooltips) {
            Component label1 = Component.translatable(OWNER_LABEL, state.ownerName);
            renderLabel(state, cameraState, queue, label1, 0.0, 0.9, 0.0, 12, matrices, light);
            if (!trade.empty) {
                Component label2 = Component.translatable(SELLING_LABEL, trade.sellingLabel);
                Component label3 = Component.translatable(PRICE_LABEL, trade.priceLabel);
                renderLabel(state, cameraState, queue, label2, 0.0, 0.9 - 0.25, 0.0, 12, matrices, light);
                renderLabel(state, cameraState, queue, label3, 0.0, 0.9 - 0.5, 0.0, 12, matrices, light);
            }
        }
    }

    private void renderLabel(
        TradingStationRenderState state, CameraRenderState cameraState, SubmitNodeCollector queue,
        Component label, double x, double y, double z,
        int maxDistance, PoseStack matrices, int light
    ) {
        double dist = state.blockPos.distToCenterSqr(cameraState.pos.x, cameraState.pos.y, cameraState.pos.z);
        if (dist < maxDistance * maxDistance) {
            matrices.pushPose();
            matrices.translate(x + 0.5, y + 1.5, z + 0.5);
            matrices.mulPose(cameraState.orientation);
            matrices.scale(+0.025f, -0.025f, +0.025f);

            float opacity = Minecraft.getInstance().options.getBackgroundOpacity(0.25f);
            int backgroundColor = Colors.color(0x000000, opacity);
            var textX = -textRenderer.width(label) * 0.5f;
            FormattedCharSequence labelOrdered = label.getVisualOrderText();
            queue.submitText(matrices, textX, 0f, labelOrdered, false, Font.DisplayMode.SEE_THROUGH, light, 0x20_FFFFFF, backgroundColor, Colors.TRANSPARENT);
            queue.submitText(matrices, textX, 0f, labelOrdered, false, Font.DisplayMode.NORMAL, light, Colors.WHITE, Colors.TRANSPARENT, Colors.TRANSPARENT);

            matrices.popPose();
        }
    }
}

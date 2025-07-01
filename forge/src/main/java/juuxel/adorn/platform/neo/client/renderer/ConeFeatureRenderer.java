package juuxel.adorn.platform.neo.client.renderer;

import juuxel.adorn.client.renderer.ConeEntityRenderer;
import juuxel.adorn.item.AdornItems;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public final class ConeFeatureRenderer<S extends BipedEntityRenderState, M extends BipedEntityModel<S>> extends FeatureRenderer<S, M> {
    public ConeFeatureRenderer(FeatureRendererContext<S, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, S state, float limbAngle, float limbDistance) {
        ItemStack headStack = state.equippedHeadStack;
        if (headStack.isOf(AdornItems.CONE.get())) {
            ConeEntityRenderer.renderOnHead(matrices, vertexConsumers, headStack, light, getContextModel());
        }
    }
}

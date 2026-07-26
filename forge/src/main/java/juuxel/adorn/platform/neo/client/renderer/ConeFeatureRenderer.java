package juuxel.adorn.platform.neo.client.renderer;

import juuxel.adorn.client.renderer.ConeEntityRenderer;
import juuxel.adorn.item.AdornItems;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemStack;

public final class ConeFeatureRenderer<S extends HumanoidRenderState, M extends HumanoidModel<S>> extends RenderLayer<S, M> {
    public ConeFeatureRenderer(RenderLayerParent<S, M> context) {
        super(context);
    }

    @Override
    public void submit(PoseStack matrices, SubmitNodeCollector queue, int light, S state, float limbAngle, float limbDistance) {
        ItemStack headStack = state.headEquipment;
        if (headStack.is(AdornItems.CONE.get())) {
            ConeEntityRenderer.renderOnHead(matrices, queue, headStack, light, state.outlineColor, getParentModel());
        }
    }
}

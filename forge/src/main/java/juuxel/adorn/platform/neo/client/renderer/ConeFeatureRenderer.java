package juuxel.adorn.platform.neo.client.renderer;

import juuxel.adorn.client.renderer.ConeEntityRenderer;
import juuxel.adorn.item.ConeItem;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public final class ConeFeatureRenderer<T extends LivingEntity, M extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {
    public ConeFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        ItemStack headStack = entity.getEquippedStack(EquipmentSlot.HEAD);
        if (headStack.getItem() instanceof ConeItem) {
            ConeEntityRenderer.renderOnHead(matrices, vertexConsumers, headStack, light, getContextModel());
        }
    }
}

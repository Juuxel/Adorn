package juuxel.adorn.client.renderer;

import juuxel.adorn.entity.AdornEntities;
import juuxel.adorn.item.AdornItems;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;

public final class AdornEntityRenderers {
    public static void init() {
        EntityRendererRegistry.register(AdornEntities.SEAT.get(), InvisibleEntityRenderer::new);
        EntityRendererRegistry.register(AdornEntities.CONE.get(), ConeEntityRenderer::new);

        ArmorRenderer.register(new ConeArmorRenderer(), AdornItems.CONES.values().toArray(ItemConvertible[]::new));
    }

    private static final class ConeArmorRenderer implements ArmorRenderer {
        @Override
        public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
            if (slot == EquipmentSlot.HEAD) {
                ConeEntityRenderer.renderOnHead(matrices, vertexConsumers, stack, light, contextModel);
            }
        }

        @Override
        public boolean shouldRenderDefaultHeadItem(LivingEntity entity, ItemStack stack) {
            return false;
        }
    }
}

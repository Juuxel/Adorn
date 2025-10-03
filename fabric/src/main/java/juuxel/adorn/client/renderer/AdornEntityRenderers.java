package juuxel.adorn.client.renderer;

import juuxel.adorn.entity.AdornEntities;
import juuxel.adorn.item.AdornItems;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public final class AdornEntityRenderers {
    public static void init() {
        EntityRendererRegistry.register(AdornEntities.SEAT.get(), InvisibleEntityRenderer::new);
        EntityRendererRegistry.register(AdornEntities.CONE.get(), ConeEntityRenderer::new);

        ArmorRenderer.register(new ConeArmorRenderer(), AdornItems.CONE.get());
    }

    private static final class ConeArmorRenderer implements ArmorRenderer {
        @Override
        public void render(MatrixStack matrices, OrderedRenderCommandQueue queue, ItemStack stack, BipedEntityRenderState renderState, EquipmentSlot slot, int light, BipedEntityModel<BipedEntityRenderState> contextModel) {
            if (slot == EquipmentSlot.HEAD) {
                ConeEntityRenderer.renderOnHead(matrices, queue, stack, light, renderState.outlineColor, contextModel);
            }
        }

        @Override
        public boolean shouldRenderDefaultHeadItem(LivingEntity entity, ItemStack stack) {
            return false;
        }
    }
}

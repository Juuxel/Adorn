package juuxel.adorn.client.renderer;

import juuxel.adorn.entity.AdornEntities;
import juuxel.adorn.item.AdornItems;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class AdornEntityRenderers {
    public static void init() {
        EntityRendererRegistry.register(AdornEntities.SEAT.get(), InvisibleEntityRenderer::new);
        EntityRendererRegistry.register(AdornEntities.CONE.get(), ConeEntityRenderer::new);

        ArmorRenderer.register(new ConeArmorRenderer(), AdornItems.CONE.get());
    }

    private static final class ConeArmorRenderer implements ArmorRenderer {
        @Override
        public void render(PoseStack matrices, SubmitNodeCollector queue, ItemStack stack, HumanoidRenderState renderState, EquipmentSlot slot, int light, HumanoidModel<HumanoidRenderState> contextModel) {
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

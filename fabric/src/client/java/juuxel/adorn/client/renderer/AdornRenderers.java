package juuxel.adorn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.entity.AdornEntities;
import juuxel.adorn.item.AdornItems;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class AdornRenderers {
    public static void init() {
        BlockEntityRenderers.register(AdornBlockEntities.TRADING_STATION.get(), TradingStationRenderer::new);
        BlockEntityRenderers.register(AdornBlockEntities.SHELF.get(), ShelfRenderer::new);
        BlockEntityRenderers.register(AdornBlockEntities.KITCHEN_SINK.get(), KitchenSinkRenderer::new);

        EntityRenderers.register(AdornEntities.SEAT.get(), InvisibleEntityRenderer::new);
        EntityRenderers.register(AdornEntities.CONE.get(), ConeEntityRenderer::new);

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

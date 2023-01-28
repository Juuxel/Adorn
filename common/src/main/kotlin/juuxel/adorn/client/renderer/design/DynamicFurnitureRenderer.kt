package juuxel.adorn.client.renderer.design

import juuxel.adorn.block.entity.DynamicFurnitureBlockEntity
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.math.MatrixStack

class DynamicFurnitureRenderer(context: BlockEntityRendererFactory.Context) : BlockEntityRenderer<DynamicFurnitureBlockEntity> {
    override fun render(
        entity: DynamicFurnitureBlockEntity, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlay: Int
    ) {
        for (part in entity.parts) {
            FurnitureDesignRenderer.drawPart(matrices, part, false, light)
        }
    }
}

package juuxel.adorn.platform.forge.client.renderer

import juuxel.adorn.client.renderer.KitchenSinkRenderer
import juuxel.adorn.platform.forge.block.entity.KitchenSinkBlockEntityForge
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory

class KitchenSinkRendererForge(context: BlockEntityRendererFactory.Context) : KitchenSinkRenderer<KitchenSinkBlockEntityForge>(context) {
    override fun getFluidLevel(entity: KitchenSinkBlockEntityForge): Double =
        entity.tank.fluidAmount.toDouble()

    override fun isEmpty(entity: KitchenSinkBlockEntityForge): Boolean =
        entity.tank.isEmpty
}

package juuxel.adorn.client.renderer

import juuxel.adorn.block.entity.KitchenSinkBlockEntityFabric
import juuxel.adorn.fluid.FluidUnit
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory

class KitchenSinkRendererFabric(context: BlockEntityRendererFactory.Context) : KitchenSinkRenderer<KitchenSinkBlockEntityFabric>(context) {
    override fun getFluidLevel(entity: KitchenSinkBlockEntityFabric): Double =
        FluidUnit.convertAsDouble(entity.storage.amount.toDouble(), from = FluidUnit.DROPLET, to = FluidUnit.LITRE)

    override fun isEmpty(entity: KitchenSinkBlockEntityFabric): Boolean =
        entity.storage.amount == 0L
}

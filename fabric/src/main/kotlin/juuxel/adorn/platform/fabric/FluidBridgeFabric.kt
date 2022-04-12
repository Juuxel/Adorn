package juuxel.adorn.platform.fabric

import juuxel.adorn.fluid.FluidUnit
import juuxel.adorn.item.AdornBucketItem
import juuxel.adorn.platform.FluidBridge
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.FluidBlock
import net.minecraft.fluid.FlowableFluid
import net.minecraft.item.Item

object FluidBridgeFabric : FluidBridge {
    override val fluidUnit = FluidUnit.DROPLET

    override fun createFluidBlock(fluid: () -> FlowableFluid, settings: AbstractBlock.Settings): Block =
        FluidBlock(fluid(), settings)

    override fun createBucket(fluid: () -> FlowableFluid, settings: Item.Settings): Item =
        AdornBucketItem(fluid, settings)
}

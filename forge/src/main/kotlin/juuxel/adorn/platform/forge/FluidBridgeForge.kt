package juuxel.adorn.platform.forge

import juuxel.adorn.fluid.FluidUnit
import juuxel.adorn.platform.FluidBridge
import juuxel.adorn.platform.forge.item.AdornBucketItem
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.FluidBlock
import net.minecraft.fluid.FlowableFluid
import net.minecraft.item.Item

object FluidBridgeForge : FluidBridge {
    override val fluidUnit = FluidUnit.LITRE

    override fun createFluidBlock(fluid: () -> FlowableFluid, settings: AbstractBlock.Settings): Block =
        FluidBlock(fluid, settings)

    override fun createBucket(fluid: () -> FlowableFluid, settings: Item.Settings): Item =
        AdornBucketItem(fluid, settings)
}

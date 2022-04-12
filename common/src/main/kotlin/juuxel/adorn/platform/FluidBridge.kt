package juuxel.adorn.platform

import juuxel.adorn.fluid.FluidUnit
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.fluid.FlowableFluid
import net.minecraft.item.Item

interface FluidBridge {
    val fluidUnit: FluidUnit

    /**
     * Creates a fluid block from a lazy flowable fluid supplier.
     * The fluid is eagerly evaluated on Fabric and lazily evaluated on Forge.
     */
    fun createFluidBlock(fluid: () -> FlowableFluid, settings: AbstractBlock.Settings): Block

    /**
     * Creates a bucket item from a lazy flowable fluid supplier.
     * The fluid is eagerly evaluated on Fabric and lazily evaluated on Forge.
     */
    fun createBucket(fluid: () -> FlowableFluid, settings: Item.Settings): Item

    companion object {
        fun get(): FluidBridge = PlatformBridges.get().fluids
    }
}

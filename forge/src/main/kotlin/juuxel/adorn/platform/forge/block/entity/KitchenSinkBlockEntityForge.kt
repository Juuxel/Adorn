package juuxel.adorn.platform.forge.block.entity

import juuxel.adorn.block.entity.KitchenSinkBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.fluid.Fluids
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fluids.capability.IFluidHandler

class KitchenSinkBlockEntityForge(pos: BlockPos, state: BlockState) : KitchenSinkBlockEntity(pos, state) {

    override fun <T> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        return if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            InfiniteWaterFluidHandler.holder.cast()
        } else {
            super.getCapability(cap, side)
        }
    }

    private object InfiniteWaterFluidHandler : IFluidHandler {
        val holder: LazyOptional<IFluidHandler> = LazyOptional.of { InfiniteWaterFluidHandler }

        override fun getTanks(): Int {
            return 1
        }

        override fun getFluidInTank(tank: Int): FluidStack {
            return FluidStack(Fluids.WATER, Int.MAX_VALUE)
        }

        override fun getTankCapacity(tank: Int): Int {
            return Int.MAX_VALUE
        }

        override fun isFluidValid(tank: Int, stack: FluidStack): Boolean {
            return tank == 0 && stack.fluid == Fluids.WATER
        }

        override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int {
            return if (resource.fluid == Fluids.WATER) resource.amount else 0
        }

        override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
            return if (resource.fluid == Fluids.WATER) resource else FluidStack.EMPTY
        }

        override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack {
            return FluidStack(Fluids.WATER, maxDrain)
        }
    }
}

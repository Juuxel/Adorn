package juuxel.adorn.platform.forge

import juuxel.adorn.platform.KitchenSinkEjection
import net.minecraft.fluid.Fluids
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fluids.capability.IFluidHandler

object KitchenSinkEjectionImpl : KitchenSinkEjection {
    override fun eject(world: World, pos: BlockPos) {
        for (direction in Direction.values()) {
            world.getBlockEntity(pos.offset(direction))?.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)?.ifPresent {
                it.fill(FluidStack(Fluids.WATER, Int.MAX_VALUE), IFluidHandler.FluidAction.EXECUTE)
            }
        }
    }
}
